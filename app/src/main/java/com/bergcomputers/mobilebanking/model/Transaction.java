package com.bergcomputers.mobilebanking.model;

/**
 * Created by berg on 5/15/2017.
 */
import android.accounts.Account;

import java.io.Serializable;
import java.util.Date;


/**
 * Entity implementation class for Entity: Transaction
 *
 */


public class Transaction extends BaseEntity implements Serializable, ITransaction  {

    public static final String FIELD_ID="id";
    public static final String FIELD_DATE="date";
    public static final String FIELD_TRANSACTION_TYPE="type";
    public static final String FIELD_AMOUNT="amount";
    public static final String FIELD_SENDER="sender";
    public static final String FIELD_DETAILS="details";
    public static final String FIELD_STATUS="status";

    private static final long serialVersionUID = -7944505705705785135L;
    public final static String findAll = "com.bergcomputers.domain.transaction.findAll";



    private Date date;
    private Account account;
    private String transactionType;
    private Double amount;
    private String sender;
    private String details;
    private String status;

    public Transaction(){
        super();
    }






    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public Date getTransactionDate() {

        return this.date;
    }
    @Override
    public void setTransactionDate(Date Date) {

        this.date=Date;
    }
    @Override
    public String getTransactionType() {

        return this.transactionType;
    }
    @Override
    public void setTransactionType(String type) {

        this.transactionType=type;
    }
    @Override
    public Double getAmount() {

        return this.amount;
    }
    @Override
    public void setAmount(Double amount) {

        this.amount=amount;
    }
    @Override
    public String getSender() {

        return this.sender;
    }
    @Override
    public void setSender(String sender) {

        this.sender=sender;
    }
    @Override
    public String getDetails() {
        // TODO Auto-generated method stub
        return this.details;
    }
    @Override
    public void setDetails(String details) {
        // TODO Auto-generated method stub
        this.details=details;
    }
    @Override
    public String getStatus() {
        // TODO Auto-generated method stub
        return this.status;
    }
    @Override
    public void setStatus(String status) {
        // TODO Auto-generated method stub
        this.status=status;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((account == null) ? 0 : account.hashCode());
        result = prime * result + ((amount == null) ? 0 : amount.hashCode());
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((details == null) ? 0 : details.hashCode());
        result = prime * result + ((sender == null) ? 0 : sender.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((transactionType == null) ? 0 : transactionType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Transaction other = (Transaction) obj;
        if (account == null) {
            if (other.account != null)
                return false;
        } else if (!account.equals(other.account))
            return false;
        if (amount == null) {
            if (other.amount != null)
                return false;
        } else if (!amount.equals(other.amount))
            return false;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (details == null) {
            if (other.details != null)
                return false;
        } else if (!details.equals(other.details))
            return false;
        if (sender == null) {
            if (other.sender != null)
                return false;
        } else if (!sender.equals(other.sender))
            return false;
        if (status == null) {
            if (other.status != null)
                return false;
        } else if (!status.equals(other.status))
            return false;
        if (transactionType == null) {
            if (other.transactionType != null)
                return false;
        } else if (!transactionType.equals(other.transactionType))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Transaction [date=" + date + ", account=" + account + ", type="
                + transactionType + ", amount=" + amount + ", sender=" + sender
                + ", details=" + details + ", status=" + status + "]";
    }


}