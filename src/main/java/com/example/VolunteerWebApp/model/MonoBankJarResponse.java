package com.example.VolunteerWebApp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonoBankJarResponse {
    private int jarAmount;
    private int jarGoal;
    private boolean isOpened;
}
