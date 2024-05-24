package com.example.VolunteerWebApp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ParsedPostModel {
    private String postName;
    private String url;
    private String description;
    private String platformName;

    private String iconUrl;
}