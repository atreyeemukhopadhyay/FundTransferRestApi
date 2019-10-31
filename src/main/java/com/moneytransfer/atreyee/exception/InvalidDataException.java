package com.moneytransfer.atreyee.exception;

public class InvalidDataException extends Exception {
    private String message;

    public InvalidDataException(String message){
        super(message);
    }
}
