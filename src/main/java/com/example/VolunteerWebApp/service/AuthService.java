package com.example.VolunteerWebApp.service;

import com.example.VolunteerWebApp.DTO.RegisterRequest;
import com.example.VolunteerWebApp.entity.User;
import com.example.VolunteerWebApp.entity.VerificationToken;
import com.example.VolunteerWebApp.model.NotificationEmail;
import com.example.VolunteerWebApp.repository.UserRepository;
import com.example.VolunteerWebApp.repository.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
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
//
//        mailService.sendMail(new NotificationEmail("Account activation", user.getEmail(),
//                "Thank you for signing up. Please click on this lind for activate your account:" +
//                        " http://localhost:8080/api/auth/accountVerification/" + token ));
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
}
