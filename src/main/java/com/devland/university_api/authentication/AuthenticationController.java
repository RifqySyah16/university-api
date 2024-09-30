package com.devland.university_api.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.devland.university_api.applicationuser.ApplicationUserService;
import com.devland.university_api.applicationuser.model.ApplicationUser;
import com.devland.university_api.applicationuser.model.dto.RegistrasionRequestDTO;
import com.devland.university_api.authentication.dto.JwtResponseDTO;
import com.devland.university_api.authentication.dto.LoginRequestDTO;
import com.devland.university_api.authentication.jwt.JwtProvider;
import com.devland.university_api.file.FileService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final ApplicationUserService applicationUserService;
    private final FileService fileService;

    @PostMapping("/tokens")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        logger.info("Attemp Login To System");
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = this.jwtProvider.generateJwtToken(authentication);

        return ResponseEntity.ok(new JwtResponseDTO(jwt));
    }

    @PostMapping(value = "/register")
    public ResponseEntity<JwtResponseDTO> registrasion(
            @RequestPart("registrasionRequestDTO") @Valid RegistrasionRequestDTO registrasionRequestDTO,
            @RequestPart("file") MultipartFile file) {
        ApplicationUser newUser = registrasionRequestDTO.convertToEntity();
        if (file != null && !file.isEmpty()) {
            this.fileService.savePhoto(newUser, file);
        }

        this.applicationUserService.save(newUser);

        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(registrasionRequestDTO.getUsername(),
                        registrasionRequestDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = this.jwtProvider.generateJwtToken(authentication);

        return ResponseEntity.status(HttpStatus.CREATED).body(new JwtResponseDTO(jwt));

    }

}
