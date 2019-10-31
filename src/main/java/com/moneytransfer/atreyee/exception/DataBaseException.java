package com.moneytransfer.atreyee.exception;

public class DataBaseException extends RuntimeException {

    private String message;

    public DataBaseException(String message) {
        super(message);
    }

}
