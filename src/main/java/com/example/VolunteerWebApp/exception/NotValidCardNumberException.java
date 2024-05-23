package com.example.VolunteerWebApp.exception;

public class NotValidCardNumberException extends RuntimeException{
    public NotValidCardNumberException(String exMessage){
        super(exMessage);
    }
}
