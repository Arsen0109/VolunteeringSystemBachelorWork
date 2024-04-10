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
public class PostResponse {
    private long postId;
    private String postName;
    private String description;
    private String monoBankJarLink;
    private String username;
    private Instant createdDate;
}
