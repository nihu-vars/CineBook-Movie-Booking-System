package com.example.booking.repository;

import com.example.booking.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    UserModel findByUserEmail(String username);
}
