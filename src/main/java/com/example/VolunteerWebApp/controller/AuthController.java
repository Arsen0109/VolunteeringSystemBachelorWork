package com.example.VolunteerWebApp.controller;

import com.example.VolunteerWebApp.DTO.AuthResponse;
import com.example.VolunteerWebApp.DTO.LoginRequest;
import com.example.VolunteerWebApp.DTO.RefreshTokenRequest;
import com.example.VolunteerWebApp.DTO.RegisterRequest;
import com.example.VolunteerWebApp.service.AuthService;
import com.example.VolunteerWebApp.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest request){
        authService.signup(request);
        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }

    @GetMapping("accountVerification/{verificationToken}")
    public ResponseEntity<String> activateUser(@PathVariable String verificationToken) {
        authService.activateUser(verificationToken);
        return new ResponseEntity<String>("Account activated succesfully", HttpStatus.OK);
    }

    @PostMapping("login")
    public AuthResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("refresh/token")
    public ResponseEntity<AuthResponse> refreshTokens(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.refreshToken(refreshTokenRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK).body("Refresh Token Deleted Successfully!");
    }
}
