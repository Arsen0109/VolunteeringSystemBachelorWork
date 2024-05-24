package com.example.VolunteerWebApp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParsedPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    private String postName;
    private String url;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String platformName;
    @Column(columnDefinition = "TEXT")
    private String iconUrl;
}