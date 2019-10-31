package com.moneytransfer.atreyee.exception;

public class InvalidUserInputException extends Exception {
    private String message;

    public InvalidUserInputException(String message){
        super(message);
    }

}
