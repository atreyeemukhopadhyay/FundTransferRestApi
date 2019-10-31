package com.moneytransfer.atreyee.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Accounts")
public class AccountDetails {

    @DatabaseField(id = true)
    private String iban;

    @DatabaseField(canBeNull = false)
    private String bic;

    @DatabaseField(canBeNull = false)
    private String accountHolderName;

    @DatabaseField(canBeNull = false)
    private String currency;

    @DatabaseField(canBeNull = false)
    private Double balance;

    public AccountDetails() {

    }

    @Override
    public String toString() {
        return getIban() + " -- " + getAccountHolderName() + " -- " + getCurrency() + "-- " + getBalance();
    }

    public String getIban() {
        return iban;
    }

    public String getBic() {
        return bic;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public String getCurrency() {
        return currency;
    }

    public Double getBalance() {
        return balance;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}