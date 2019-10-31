package com.moneytransfer.atreyee.dao;

import com.moneytransfer.atreyee.exception.InvalidUserInputException;
import com.moneytransfer.atreyee.model.AccountDetails;
import com.moneytransfer.atreyee.exception.DataBaseException;
import org.eclipse.persistence.exceptions.DatabaseException;

public interface FundTransferIDAO {
    AccountDetails queryByAccount(String accountNumber) throws DatabaseException;

    int createAccount(AccountDetails newAccount) throws DataBaseException;

    int updateAccount(AccountDetails senderAccount, AccountDetails receiverAccount) throws DataBaseException;

    int clearAccountTable() throws DataBaseException;

    int dropAccountTable() throws DataBaseException;

    int createAccountTable() throws DataBaseException;
}
