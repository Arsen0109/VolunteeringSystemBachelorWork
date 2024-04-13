package com.example.VolunteerWebApp.exception;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(String exMessage) {
        super(exMessage);
    }
}
