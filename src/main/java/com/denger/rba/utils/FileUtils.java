package com.denger.rba.utils;

import com.denger.rba.entity.user.User;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {


    public static File generateFileFromUserInformation(User user, String directoryPath) throws IOException {
       File file = checkIfFileAlreadyExist(user,directoryPath);
       if(file == null){
           return generateNewFile(user,directoryPath);
       }
        return file;
    }

    public static File generateNewFile(User user,String directoryPath) throws IOException {
        String folderName = user.getOib();
        String fileName = folderName + File.separator + user.getOib() + "_" + user.getSurname() + "_" + user.getCardInformation().getLastModified() + ".txt";
        File folder = new File(directoryPath, folderName);

        if (!folder.exists()) {
            folder.mkdirs();
        }

        File file = new File(directoryPath, fileName);

        if (!file.exists()) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("Name:" + user.getName() + ";");
                writer.write("Surname:" + user.getSurname() + ";");
                writer.write("OIB:" + user.getOib() + ";");
                writer.write("Status:" + user.getCardInformation().isStatus() + ";");
            }
        }

        return file;
    }

    private static File checkIfFileAlreadyExist(User user,String directoryPath) {
        String fileName = directoryPath + user.getOib() + "_" + user.getSurname() + "_" + user.getCardInformation().getLastModified() + ".txt";
        File file = new File(fileName);

        if (file.exists()) {
            return file;
        }

        return null;
    }

    public static void setFileToInactive(User user, String directoryPath) {
        String folderName = user.getOib();
        String fileName = folderName + File.separator + user.getOib() + "_" + user.getSurname() + "_" + user.getCardInformation().getLastModified() + ".txt";
        File file = new File(directoryPath, fileName);

        if (file.exists()) {
            String newFileName = fileName.replace(".txt", "_inactive.txt");
            File newFile = new File(directoryPath, newFileName);
            file.renameTo(newFile);
        }
    }
}
