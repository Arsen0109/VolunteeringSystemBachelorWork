package com.example.VolunteerWebApp.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    private String postName;
    @Column(columnDefinition = "TEXT")
    @Nullable
    private String description;
    @Nullable
    private String monobankJarLink;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;
    private String cardNumber;
    private Boolean isOpened;
    private Instant createdDate;
}
