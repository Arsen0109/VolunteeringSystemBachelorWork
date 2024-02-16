package com.example.VolunteerWebApp.controller;

import com.example.VolunteerWebApp.DTO.RegisterRequest;
import com.example.VolunteerWebApp.entity.User;
import com.example.VolunteerWebApp.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

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
}
