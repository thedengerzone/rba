package com.denger.rba.controller;

import com.denger.rba.entity.user.User;
import com.denger.rba.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "/v1/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            userService.createUser(user);
            logger.info("Registration request processed successfully");
            return ResponseEntity.ok("User successfully created");
        } catch (Exception e) {
            logger.error("Error occurred during registration: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }

    @RequestMapping(value = "/card", method = POST,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> generateUserInformationByOib(@RequestBody String oib) {
        try {
            File cardFile = userService.generate(oib);
            if (cardFile != null) {
                Resource resource = new FileSystemResource(cardFile);
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(Objects.requireNonNull(resource.getFilename()), StandardCharsets.UTF_8))
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error occurred during generation of user information: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @RequestMapping(value = "/delete", method = POST)
    public ResponseEntity<String> deleteUser(@RequestBody String oib) {
        try {
            userService.deleteUser(oib);
            logger.info("Deletion request processed successfully");
            return ResponseEntity.ok("User successfully deleted");
        } catch (Exception e) {
            logger.error("Error occurred during deletion: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }
}
