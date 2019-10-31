package com.moneytransfer.atreyee.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FundTransferRequestModel {
    private String fromAccount;
    private String toAccount;
    private Double amount;

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}