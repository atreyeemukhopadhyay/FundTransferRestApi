package com.moneytransfer.atreyee.service;

import com.moneytransfer.atreyee.model.AccountDetails;
import com.moneytransfer.atreyee.dao.FundTransferDAOImpl;
import com.moneytransfer.atreyee.dao.FundTransferIDAO;
import com.moneytransfer.atreyee.exception.DataBaseException;
import com.moneytransfer.atreyee.exception.InvalidDataException;
import com.moneytransfer.atreyee.exception.InvalidUserInputException;
import com.moneytransfer.atreyee.exception.TransactionMessages;
import com.moneytransfer.atreyee.model.FundTransferRequestModel;
import com.moneytransfer.atreyee.model.FundTransferResponseModel;
import com.moneytransfer.atreyee.util.Currency;
import org.apache.log4j.Logger;
import org.eclipse.persistence.exceptions.DatabaseException;

import java.sql.SQLException;
import java.util.Map;

public class FundTransferServiceImpl implements FundTransferService {
    static final Logger log = Logger.getLogger(FundTransferServiceImpl.class);

    FundTransferIDAO ftDAO;

    public FundTransferServiceImpl() throws SQLException {
        log.debug("Instantiating FundTransferDAOImpl");
        this.ftDAO = new FundTransferDAOImpl();
    }

    public AccountDetails debitAccount(String accountNumber, Double transferAmount) throws DatabaseException {
        log.debug("populating debit account details");
        AccountDetails account = ftDAO.queryByAccount(accountNumber);
        Double ledgerBalance = account.getBalance();
        Double availableBalance = (ledgerBalance - transferAmount);
        account.setBalance(availableBalance);
        return account;
    }

    public AccountDetails creditAccount(String accountNumber, Double transferAmount) throws DatabaseException {
        log.debug("populating credit account details");
        AccountDetails account = ftDAO.queryByAccount(accountNumber);
        Double ledgerBalance = account.getBalance();
        Double availableBalance = (ledgerBalance + transferAmount);
        account.setBalance(availableBalance);
        return account;
    }

    public AccountDetails getAccountDetails(String accountNumber) throws DatabaseException {
        log.debug("getAccountDetails for" + accountNumber);
        return ftDAO.queryByAccount(accountNumber);
    }

    public void printBalance(String accountNumber) {
        log.debug("printBalance for" + accountNumber);
        try {
            AccountDetails account = ftDAO.queryByAccount(accountNumber);
            if (account != null) {
                System.out.println(String.format("Account %s has balance %f", account.getIban(), account.getBalance()));
            }
        } catch (Exception ex) {
            log.error("exception in printBalance for" + ex.getMessage());
        }
    }

    public synchronized FundTransferResponseModel transferMoney(FundTransferRequestModel userInput) {
        log.debug("transferMoney called");
        FundTransferResponseModel response = new FundTransferResponseModel();

        String senderAccountNumber = userInput.getFromAccount();
        String receiverAccountNumber = userInput.getToAccount();
        Double transferAmount = userInput.getAmount();

        try {
            AccountDetails senderAccount = this.getAccountDetails(senderAccountNumber);
            AccountDetails receiverAccount = this.getAccountDetails(receiverAccountNumber);


            if (senderAccount != null && receiverAccount != null) {
                log.debug("sender account details:" + senderAccount.toString());
                log.debug("receiver account details:" + receiverAccount.toString());
                if (senderAccount.getBalance() > transferAmount) {
                    String senderAccountCurrency = senderAccount.getCurrency();
                    String receiverAccountCurrency = receiverAccount.getCurrency();
                    Double convertedTransferAmount = transferAmount;

                    if (senderAccountCurrency != "" && receiverAccountCurrency != "" && !senderAccountCurrency.equalsIgnoreCase(receiverAccountCurrency)) {
                        Map currencyConversionMap = Currency.getCurrentConversionValues();
                        convertedTransferAmount = Currency.convert(transferAmount, ((Map<String, Double>) currencyConversionMap.get(senderAccountCurrency)).get(receiverAccountCurrency));
                    } else {
                        throw new InvalidDataException(TransactionMessages.REQUIRED_DATA_MISSING.getErrorMessage());
                    }
                    if (this.initiateTransfer(this.debitAccount(senderAccountNumber, transferAmount), this.creditAccount(receiverAccountNumber, convertedTransferAmount)) == 0) {
                        response.setResponseMessage(TransactionMessages.SUCCESSFUL_TRANSACTION.getErrorMessage());
                        response.setResponseCode(200);
                        this.printBalance(senderAccountNumber);
                        this.printBalance(receiverAccountNumber);
                    }
                } else {
                    log.error("insufficient balance");
                    throw new InvalidUserInputException(TransactionMessages.INSUFFICIENT_AMOUNT.getErrorMessage());
                }
            } else {
                log.error("User input provided is invalid");
                throw new InvalidUserInputException(TransactionMessages.INVALID_ACCOUNT.getErrorMessage());
            }

        } catch (DataBaseException dbe) {
            log.error(dbe.getMessage());
            response.setResponseCode(500);
            response.setResponseMessage(dbe.getMessage());
        } catch (InvalidUserInputException ex) {
            log.error(ex.getMessage());
            response.setResponseCode(400);
            response.setResponseMessage(ex.getMessage());
        } catch (InvalidDataException ex) {
            log.error(ex.getMessage());
            response.setResponseCode(400);
            response.setResponseMessage(ex.getMessage());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            response.setResponseCode(500);
            response.setResponseMessage(String.format("%s : %s", TransactionMessages.INTERNAL_ERROR.getErrorMessage(), ex.getMessage()));
        }

        return response;
    }

    public int initiateTransfer(AccountDetails fromAccount, AccountDetails toAccountDetails) throws DataBaseException {
        int result = ftDAO.updateAccount(fromAccount, toAccountDetails);
        return result;
    }

    public void initializeDB() {
        try {
            ftDAO.createAccountTable();

            AccountDetails ac1 = new AccountDetails();
            ac1.setIban("GB53 REVO 0099 7052 8969 08");
            ac1.setAccountHolderName("Atreyee Mukhopadhyay");
            ac1.setBalance(5000.0);
            ac1.setCurrency("EUR");
            ac1.setBic("REVOGB21");

            ftDAO.createAccount(ac1);

            AccountDetails ac2 = new AccountDetails();
            ac2.setIban("LT22 3250 0403 2084 3273");
            ac2.setAccountHolderName("Atreyee Mukhopadhyay");
            ac2.setBalance(3000.0);
            ac2.setCurrency("GBP");
            ac2.setBic("REVOLT21");

            ftDAO.createAccount(ac2);

            AccountDetails ac3 = new AccountDetails();
            ac3.setIban("IN10 HDFC 0403 2084 3273");
            ac3.setAccountHolderName("Atreyee Mukhopadhyay");
            ac3.setBalance(10000.0);
            ac3.setCurrency("INR");
            ac3.setBic("REVOIN21");


            ftDAO.createAccount(ac3);

            AccountDetails ac4 = new AccountDetails();
            ac4.setIban("LT00 1234 4567 7890 99");
            ac4.setAccountHolderName("Atreyee Mukhopadhyay");
            ac4.setBalance(1000.0);
            ac4.setCurrency("");
            ac4.setBic("REVOIN21");


            ftDAO.createAccount(ac4);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
