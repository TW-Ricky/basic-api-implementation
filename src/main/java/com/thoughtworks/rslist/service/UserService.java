package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.User;

import java.util.List;

public interface UserService {

    boolean existsUserById(Integer id);

    Integer addUser(User user);

    List<User> getUserList();

    User getUserById(Integer index);

    void deleteAll();

    void deleteUserById(Integer index);
}
