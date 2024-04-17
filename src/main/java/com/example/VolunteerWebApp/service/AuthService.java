package com.example.VolunteerWebApp.service;

import com.example.VolunteerWebApp.DTO.AuthResponse;
import com.example.VolunteerWebApp.DTO.LoginRequest;
import com.example.VolunteerWebApp.DTO.RefreshTokenRequest;
import com.example.VolunteerWebApp.DTO.RegisterRequest;
import com.example.VolunteerWebApp.entity.User;
import com.example.VolunteerWebApp.entity.VerificationToken;
import com.example.VolunteerWebApp.model.NotificationEmail;
import com.example.VolunteerWebApp.repository.UserRepository;
import com.example.VolunteerWebApp.repository.VerificationTokenRepository;
import com.example.VolunteerWebApp.security.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;


@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void signup(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);

        String token = generateVerificationToken(user);

        mailService.sendMail(new NotificationEmail("Account activation", user.getEmail(),
                "Thank you for signing up. Please click on this lind for activate your account:" +
                        " http://localhost:8080/api/auth/accountVerification/" + token ));
    }

    public User getCurrentUser() {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        org.springframework.security.core.userdetails.User principal
                = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername()).orElseThrow(() ->
                new UsernameNotFoundException("Error user: " + principal.getUsername() + "not found!"));
    }
    public void activateUser(String token) {
        User user = verificationTokenRepository.findByToken(token).orElseThrow().getUser();
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Transactional
    private String generateVerificationToken(User user){
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateAuthToken(authentication);
        return AuthResponse.builder()
                .authToken(token)
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .username(loginRequest.getUsername())
                .build();
    }

    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenByUsername(refreshTokenRequest.getUsername());
        return AuthResponse.builder()
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .authToken(token)
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }
}
