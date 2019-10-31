package com.moneytransfer.atreyee.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.moneytransfer.atreyee.exception.InvalidUserInputException;
import com.moneytransfer.atreyee.model.AccountDetails;
import com.moneytransfer.atreyee.exception.DataBaseException;
import com.moneytransfer.atreyee.exception.TransactionMessages;
import com.moneytransfer.atreyee.util.ConfigProperties;
import org.apache.log4j.Logger;
import org.eclipse.persistence.exceptions.DatabaseException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.Callable;

public class FundTransferDAOImpl implements FundTransferIDAO {

    static final Logger log = Logger.getLogger(FundTransferDAOImpl.class);
    private static ConnectionSource cs;

    public FundTransferDAOImpl() throws SQLException {
        log.debug("instantiating FundTransferDAOImpl");
        cs = openConnection();
        log.info("db connection opened");
    }

    public AccountDetails queryByAccount(String accountNumber) throws DatabaseException {
        AccountDetails accountDetails;
        log.debug("querying db for account no. " + accountNumber);
        try {
            Dao<AccountDetails, String> accountDAO = getDAOInstance();
            log.debug("accountDAO created");
            accountDetails = accountDAO.queryForId(accountNumber);
        } catch (SQLException sqlEx) {
            log.error("Exception in queryByAccount" + sqlEx.getMessage());
            throw new DataBaseException(String.format("Error while inserting account Details : %s", sqlEx.getMessage()));
        }
        return accountDetails;
    }

    public int createAccount(AccountDetails newAccount) throws DataBaseException {
        int result;
        try {
            log.debug("inserting new account details db for account no. " + newAccount.toString());
            Dao<AccountDetails, String> accountDAO = getDAOInstance();
            result = accountDAO.create(newAccount);
            log.debug("insertion new account details in db result. " + result);
        } catch (SQLException ex) {
            log.error("Exception in createAccount" + ex.getMessage());
            throw new DataBaseException(String.format("Error while inserting account Details : %s", ex.getMessage()));
        }
        return result;
    }

    public int updateAccount(final AccountDetails senderAccount, final AccountDetails receiverAccount) throws DataBaseException {
        int result = 0;
        try {
            log.debug("updateAccount accounts details in db for account no. " + senderAccount.toString() + "--" + receiverAccount.toString());
            final Dao<AccountDetails, String> accountDAO = getDAOInstance();
            accountDAO.callBatchTasks(new Callable<Void>() {
                public Void call() throws SQLException {
                    accountDAO.update(senderAccount);
                    accountDAO.update(receiverAccount);
                    return null;
                }
            });

        } catch (SQLException sqlEx) {
            log.error("Exception in updateAccount" + sqlEx.getMessage());
            throw new DataBaseException(String.format("Error while updating account Details: %s", sqlEx.getMessage()));
        } catch (Exception ex) {
            log.error("Exception in updateAccount " + ex.getMessage());
            throw new DataBaseException(String.format("Error while updating account Details: %s", ex.getMessage()));
        }
        return result;
    }

    public int createAccountTable() throws DataBaseException {
        int result;
        try {
            log.debug("Creating account table in DB");
            result = TableUtils.createTable(cs, AccountDetails.class);
            log.debug("Account table created in DB " + result);
        } catch (SQLException sqlEx) {
            log.error("Exception in createAccountTable " + sqlEx.getMessage());
            throw new DataBaseException(String.format("Error while creating the Account Table: %s", sqlEx.getMessage()));
        }
        return result;
    }

    public int clearAccountTable() throws DataBaseException {
        int result;
        try {
            log.debug("Clearing account table in DB");
            result = TableUtils.clearTable(cs, AccountDetails.class);
            log.debug("account table cleared in DB " + result);
        } catch (SQLException sqlEx) {
            log.error("Exception in clearAccountTable " + sqlEx.getMessage());
            throw new DataBaseException(String.format("Error while clearing the Account Table: %s", sqlEx.getMessage()));
        }
        return result;
    }

    public int dropAccountTable() throws DataBaseException {
        int result;
        try {
            log.debug("Drop account table in DB");
            result = TableUtils.dropTable(cs, AccountDetails.class, false);
        } catch (SQLException sqlEx) {
            log.error("Exception in dropAccountTable " + sqlEx.getMessage());
            throw new DataBaseException(String.format("Error while deleting the table: %s", sqlEx.getMessage()));
        }
        return result;
    }

    private ConnectionSource openConnection() throws DataBaseException {
        String databaseUrl = ConfigProperties.getProps().getProperty("h2.db.url");
        String user = ConfigProperties.getProps().getProperty("user");
        String pwd = ConfigProperties.getProps().getProperty("password");

        log.debug("Opening the db connection");
        ConnectionSource connectionSource;
        try {
            connectionSource = new JdbcConnectionSource(databaseUrl, user, pwd);
        } catch (SQLException sqlEx) {
            log.error("Exception in openConnection " + sqlEx.getMessage());
            throw new DataBaseException(TransactionMessages.DB_CONNECTION_ERROR.getErrorMessage());
        }
        return connectionSource;
    }

    private void closeConnection() throws IOException {
        log.debug("Closing the db connection");
        if (cs != null) {
            cs.close();
        }
    }

    private Dao<AccountDetails, String> getDAOInstance() throws DataBaseException {
        Dao<AccountDetails, String> accountDao;
        log.debug("creating dao instance");
        try {
            accountDao =
                    DaoManager.createDao(cs, AccountDetails.class);
        } catch (SQLException sqlEx) {
            log.error("Exception in getDAOInstance " + sqlEx.getMessage());
            throw new DataBaseException(String.format("Internal Error", sqlEx.getMessage()));
        }
        return accountDao;
    }
}