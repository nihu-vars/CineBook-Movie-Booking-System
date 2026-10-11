package com.example.booking.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserPrincipal implements UserDetails {
    private UserModel userModel;
    public UserPrincipal(UserModel userModel) {
        this.userModel = userModel;
    }

    //"What permissions/roles does this user have?"
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(userModel.getRoles()));
    }

    //This tells Spring Security "What password is stored for this user?"
    //User enters userId 1 password=123 -> spring Security -> checks password
    @Override
    public String getPassword() {
        return userModel.getUserPassword();
    }

    @Override
    public String getUsername() {
        return userModel.getUserEmail();     // as we are using userEmail to generate the token
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

}
