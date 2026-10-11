package com.example.booking.config;

import com.example.booking.handler.CustomAuthenticationSuccessHandler;
import com.example.booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//creating this class to customize the filter chain that is provided by default by Spring security
//this allows us to configure the Spring security....you just need to create a Bean
@Configuration
@EnableWebSecurity  //means use this config file for the security
public class SpringSecurity {

    @Autowired
    UserService userService;

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    //This creates a Spring Security filter chain.Think of it as a list of security rules that every HTTP request passes through.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //CSRF means Cross-Site Request Forgery.Spring Security enables CSRF protection by default for browser-based applications  With CSRF enabled , requests such as Post, put delete might ask for CSRF token so we are disabling that
        http.csrf(customizer -> customizer.disable());
        //means dont ask for token when we are login or registering
        http.authorizeHttpRequests(requests -> requests
                .requestMatchers("/", "/register", "/login", "/payment", "/verifyPayment").permitAll()
                .requestMatchers("/user/movies/**", "/shows/movie/**", "/booking/show/**","/booking/create", "/user/user-details/**","/user/bookings/**", "/logOut", "/booking/payment/**").hasAnyAuthority("USER", "ADMIN")
                .anyRequest().hasAuthority("ADMIN")  //Everything else requires authentication
        );

        //telling sping security you have your own login page so use that
        http.formLogin(form -> form          //add this only when we dont want the jwt token to be generated and verify the username and password using the spring security
                .loginPage("/")
                .loginProcessingUrl("/login")
//                .defaultSuccessUrl("/mainpage", true) // now i want separate pages to exist whenever i login from admin or user , so creating a handler see below line
                .successHandler(customAuthenticationSuccessHandler)
                .failureUrl("/?error=true")
                .permitAll()
        );

        http.httpBasic(Customizer.withDefaults());
        //Put my jwtFilter then Spring Security's UsernamePasswordAuthenticationFilter then Other Spring Security Filters
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        //How do we actually verify a username and password?

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userService);
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder(10));
        return  daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration authenticationConfiguration) {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
