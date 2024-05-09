package com.example.VolunteerWebApp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    private String postName;
    private String description;
    private String monoBankJarLink;
    private Boolean isOpened;
}
