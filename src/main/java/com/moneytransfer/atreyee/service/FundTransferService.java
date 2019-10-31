package com.moneytransfer.atreyee.service;

import com.moneytransfer.atreyee.exception.InvalidUserInputException;
import com.moneytransfer.atreyee.model.AccountDetails;
import com.moneytransfer.atreyee.exception.DataBaseException;
import com.moneytransfer.atreyee.model.FundTransferRequestModel;
import com.moneytransfer.atreyee.model.FundTransferResponseModel;
import org.eclipse.persistence.exceptions.DatabaseException;

public interface FundTransferService {
    AccountDetails debitAccount(String accountNumber, Double transferAmount) throws DatabaseException;

    AccountDetails creditAccount(String accountNumber, Double transferAmount) throws DatabaseException;

    int initiateTransfer(AccountDetails fromAccount, AccountDetails toAccountDetails) throws DataBaseException;

    public FundTransferResponseModel transferMoney(FundTransferRequestModel userInput) throws DataBaseException;

    public AccountDetails getAccountDetails(String accountNumber) throws DatabaseException;

    public void initializeDB();
}
