package com.example.booking.controller;

import com.example.booking.dto.LoginRequest;
import com.example.booking.service.JwtService;
import com.example.booking.service.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
//
//RestController is mainly used when your method returns data directly.
//Controller is mainly used when your method returns a view/page
@RestController
public class UserLoginController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    @Autowired
    TokenBlacklistService tokenBlacklistService;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUseremail(), request.getPassword()));
        //if the above authentication succeeds then generate the token for user
        String token = jwtService.generateToken(request.getUseremail());
                return token;
    }

    @PostMapping("/logOut")
    public String logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenBlacklistService.addRevokeToken(token);
            return "Logged out successfully";

        }
        return "No token found";

    }

}
