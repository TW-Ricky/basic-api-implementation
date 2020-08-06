package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Error;
import com.thoughtworks.rslist.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    List<User> userList = initUserList();

    private List<User> initUserList() {
        List<User> users = new ArrayList<>();
        users.add(new User("ricky", "male", 19, "a@b.com", "18888888888"));
        return users;
    }

    public boolean checkUser(String userName) {
        long count = userList.stream().filter(it -> it.getUserName().equals(userName)).count();
        return count > 0;
    }
    public void addUser(User user) {
        userList.add(user);
    }
    @PostMapping("/user")
    public ResponseEntity registerUser(@RequestBody @Valid User user) {
        userList.add(user);
        return ResponseEntity.created(null).build();
    }

    @GetMapping("/users")
    public ResponseEntity getUserList() {
        return ResponseEntity.ok(userList);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity userNotValidExceptionHandler(MethodArgumentNotValidException e) {
        Error error = new Error("invalid user");
        return ResponseEntity.badRequest().body(error);
    }
}
