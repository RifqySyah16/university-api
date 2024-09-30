package com.devland.university_api.authentication.model;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.devland.university_api.applicationuser.model.ApplicationUser;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;

public class UserPrincipal implements UserDetails {
    @Getter
    private Long id;

    @Getter
    private String name;

    @Getter
    private String username;

    @Getter
    private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    private UserPrincipal(Long id, String name, String username, String email, String password) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = Collections.emptyList();
    }

    public static UserDetails build(ApplicationUser applicationUser) {
        return new UserPrincipal(applicationUser.getId(), applicationUser.getName(), applicationUser.getUsername(),
                applicationUser.getEmail(), applicationUser.getPassword());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

}
