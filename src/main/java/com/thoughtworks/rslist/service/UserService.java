package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.User;

import java.util.List;

public interface UserService {

    boolean checkUser(String userName);

    void addUser(User user);

    List<User> getUserList();

    User getUserByIndex(int index);

    void deleteAll();
}
