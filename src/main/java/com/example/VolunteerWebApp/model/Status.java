package com.example.VolunteerWebApp.model;

public enum Status {
    OPENED("O"), CLOSED("C");

    private String code;

    private Status(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
