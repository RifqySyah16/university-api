package com.devland.university_api.applicationuser;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.devland.university_api.applicationuser.model.ApplicationUser;
import com.devland.university_api.authentication.model.UserPrincipal;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationUserService implements UserDetailsService {
    private final ApplicationUserRepository applicationUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser applicationUser = this.applicationUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username Not Found"));

        return UserPrincipal.build(applicationUser);

    }

    public ApplicationUser save(ApplicationUser newUser) {
        Optional<ApplicationUser> existingUser = this.applicationUserRepository.findByUsername(newUser.getUsername());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistException("User Already Exist");
        }

        String rawPassword = newUser.getPassword();
        String encodedPassword = this.bCryptPasswordEncoder.encode(rawPassword);
        newUser.setPassword(encodedPassword);

        return this.applicationUserRepository.save(newUser);
    }

}
