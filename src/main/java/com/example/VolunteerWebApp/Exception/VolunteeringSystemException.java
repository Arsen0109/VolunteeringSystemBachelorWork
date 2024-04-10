package com.example.VolunteerWebApp.Exception;

public class VolunteeringSystemException extends RuntimeException{
    public VolunteeringSystemException(String exMessage, Exception exception){
        super(exMessage, exception);
    }
    public VolunteeringSystemException(String exMessage){
        super(exMessage);
    }
}
