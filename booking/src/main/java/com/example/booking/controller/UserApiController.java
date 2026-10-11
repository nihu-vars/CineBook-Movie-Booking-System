package com.example.booking.controller;

import com.example.booking.model.UserModel;
import com.example.booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserApiController {

    @Autowired
    UserService userService;

    @GetMapping("/users")
    public List<UserModel> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public UserModel getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/user/{id}")
    public UserModel updateUser(
            @RequestBody UserModel userModel,
            @PathVariable Long id) {

        userModel.setUserId(id);
        return userService.updateUser(userModel);
    }

    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PostMapping("/user")
    public void createUser(@RequestBody UserModel userModel) {
        userService.registerUser(userModel);
    }
}
