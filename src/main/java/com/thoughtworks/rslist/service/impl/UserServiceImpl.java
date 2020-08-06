package com.thoughtworks.rslist.service.impl;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.RsDataBase;
import com.thoughtworks.rslist.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    RsDataBase rsDataBase;

    @Override
    public boolean checkUser(String userName) {
        List<User> userList = rsDataBase.getUserList();
        long count = userList.stream().filter(it -> it.getUserName().equals(userName)).count();
        return count > 0;
    }

    @Override
    public void addUser(User user) {
        List<User> userList = rsDataBase.getUserList();
        userList.add(user);
        rsDataBase.setUserList(userList);
    }

    @Override
    public List<User> getUserList() {
        return rsDataBase.getUserList();
    }

    @Override
    public User getUserByIndex(int index) {
        return null;
    }

    @Override
    public void deleteAll() {
        List<User> userList = rsDataBase.getUserList();
        userList.clear();
    }
}
