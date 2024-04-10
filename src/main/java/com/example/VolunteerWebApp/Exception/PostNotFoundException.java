package com.example.VolunteerWebApp.Exception;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(String exMessage) {
        super(exMessage);
    }
}
