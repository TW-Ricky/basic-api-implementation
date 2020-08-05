package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    List<User> userList = new ArrayList<>();

    public boolean checkUser(String userName) {
        long count = userList.stream().filter(it -> it.getUserName().equals(userName)).count();
        return count > 0;
    }
    public void addUser(User user) {
        userList.add(user);
    }
    @PostMapping("/user")
    public void registerUser(@RequestBody @Valid User user) {
        userList.add(user);
    }
}
