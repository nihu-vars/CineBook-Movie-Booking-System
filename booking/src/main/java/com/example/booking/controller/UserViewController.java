package com.example.booking.controller;

import com.example.booking.model.MovieModel;
import com.example.booking.model.ShowModel;
import com.example.booking.model.UserModel;
import com.example.booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserViewController {
    @Autowired
    UserService userService;

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        List<UserModel> userModel = userService.getAllUsers();
        model.addAttribute("users", userModel);
        return "users";
    }

    @GetMapping("/users/{id}")
    public String getAnUser(Model model, @PathVariable long id) {
        UserModel userModel = userService.getUserById(id);
        model.addAttribute("user", userModel);
        return "user-details";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        return "redirect:/users";

    }

    @GetMapping("/users/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        UserModel userModel = userService.getUserById(id);
        model.addAttribute("user", userModel);
        return "updateUser";

    }

    @PostMapping("/users/update")
    public String updateUser(@ModelAttribute UserModel userModel) {
        userService.updateUser(userModel);
        return "redirect:/users";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new UserModel());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserModel userModel) {
        userService.registerUser(userModel);
        return "redirect:/";
    }


    //NOW AS WE WANT THAT USER SHOULD BE ABLE TO PERFORM CRUD OPERATIONS ON ONLY ITS USER AND NOT ANY OTHER USERS SO THAT MEANS WE HAVE TO CREATE METHODS SUCH WAY THEY DONT TAKE {id} parameter
    @GetMapping("/user/user-details")
    public String getCurrentUser(Authentication authentication, Model model) {
        String email = authentication.getName();

        UserModel user = userService.getUserByEmail(email);

        model.addAttribute("user", user);

        return "user/user-details";

    }

    @GetMapping("/user/user-details/delete")
    public String deleteCurrentUser(Authentication authentication) {
        String email = authentication.getName();

        UserModel user = userService.getUserByEmail(email);

        userService.deleteUser(user.getUserId());
        return "redirect:/";
    }

    @GetMapping("/user/user-details/edit")
    public String showCurrentUserEditForm(Authentication authentication, Model model) {
        String email = authentication.getName();
        UserModel user = userService.getUserByEmail(email);
        UserModel userModel = userService.getUserById(user.getUserId());
        model.addAttribute("user", userModel);
        return "user/updateUser";

    }

    @PostMapping("/user/user-details/update")
    public String updateCurrentUser(@ModelAttribute UserModel userModel) {
        userService.updateUser(userModel);
        return "redirect:/user/user-details";
    }
}
