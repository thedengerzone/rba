package com.denger.rba.service;


import com.denger.rba.entity.user.User;

import java.io.File;

public interface UserService {

    File generate(String oib);
    User createUser(User user);
    void deleteUser(String oib);

}
