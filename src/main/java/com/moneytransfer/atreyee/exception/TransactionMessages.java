package com.moneytransfer.atreyee.exception;

public enum TransactionMessages {

    INVALID_ACCOUNT("Invalid Sender or Receiver account"),
    INSUFFICIENT_AMOUNT("Sufficient amount is not available for transfer"),
    DB_CONNECTION_ERROR("Error while opening the database connection.Check the db details provided."),
    INTERNAL_ERROR("Internal error."),
    SUCCESSFUL_TRANSACTION("transaction successful"),
    REQUIRED_DATA_MISSING("Information required for transaction is missing");

    TransactionMessages(String errorMessage) {
        this.errorMessage = errorMessage;

    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    private String errorMessage;


};
