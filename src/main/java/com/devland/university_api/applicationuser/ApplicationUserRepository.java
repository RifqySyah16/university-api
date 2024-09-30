package com.devland.university_api.applicationuser;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devland.university_api.applicationuser.model.ApplicationUser;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {

    Optional<ApplicationUser> findByUsername(String username);

}
