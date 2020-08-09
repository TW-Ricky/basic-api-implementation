package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/user")
    public ResponseEntity<Integer> registerUser(@RequestBody @Valid User user) throws Exception {
        Integer id = userService.addUser(user);
        return ResponseEntity.created(null).body(id);
    }

    @GetMapping("/users")
    public ResponseEntity<List> getUserList() {
        return ResponseEntity.ok(userService.getUserList());
    }

    @GetMapping("/user/{index}")
    public ResponseEntity<User> getUserById(@PathVariable int index) {
        return ResponseEntity.ok(userService.getUserById(index));
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity deleteUserById(@PathVariable int id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }

}
