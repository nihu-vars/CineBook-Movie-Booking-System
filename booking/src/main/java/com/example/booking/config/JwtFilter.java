package com.example.booking.config;

import com.example.booking.service.JwtService;
import com.example.booking.service.TokenBlacklistService;
import com.example.booking.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component //so spring can manage it
public class JwtFilter extends OncePerRequestFilter {   //OncePerRequestFilter means This is a Spring class that ensures your filter runs once for each HTTP request.
    @Autowired
    JwtService jwtService;
    @Autowired
    UserService userService;
    @Autowired
    TokenBlacklistService tokenBlacklistService;


    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //get the Authorization header from the request
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        //extracting token and username from the token
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtService.extractSubject(token);
        }

        //checking if somebody is using that token from that session where the user has been logged out
        if (token != null && tokenBlacklistService.isTokenRevoked(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        //checking if the token is valid or user is authenticated
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {    //SecurityContextHolder.getContext().getAuthentication() == null -- this means that if the request is already authenticated then no need to verify it again
            UserDetails userDetails = userService.loadUserByUsername(username);
            if(jwtService.validateToken(token, userDetails)) {
                //now we are entering here cause the user has been authenticated
                //Create an Authentication object
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); // as the user is authenticated we are sending userDetails, null(cause we dont need password as the user is already authenticated) and the authorities it has like USER or ADMIN

                //adding additional information about the request like sessionId
                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                //Put the authenticated user into Spring Security  You're telling Spring Security: "This request has been authenticated, and this is the authenticated user."
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            }
        }
        filterChain.doFilter(request, response); // means this filter is done now go to another filter
    }

}
