package com.example.VolunteerWebApp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long tokenID;
    private String token;
    @OneToOne(fetch = FetchType.LAZY)
    private User user;
    private Instant expiryDate;
}
