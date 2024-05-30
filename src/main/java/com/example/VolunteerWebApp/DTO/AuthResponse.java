package com.example.VolunteerWebApp.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String authToken;
    private String refreshToken;
    private Instant expiresAt;
    private String username;
    private boolean isAdmin;
}
