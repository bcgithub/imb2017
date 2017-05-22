package com.bergcomputers.mobilebanking.model;

/**
 * Created by Mario on 15-May-17.
 */

import java.io.Serializable;

public class Account extends BaseEntity {
    public static final String FIELD_ID="id";
    public static final String FIELD_IBAN="iban";
    public static final String FIELD_AMOUNT="amount";

    private String iban;

    private Double amount;

    @Override
    public String toString() {
        return "Account [iban=" + iban + ", amount=" + amount + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((amount == null) ? 0 : amount.hashCode());
        result = prime * result + ((iban == null) ? 0 : iban.hashCode());
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
        Account other = (Account) obj;
        if (amount == null) {
            if (other.amount != null)
                return false;
        } else if (!amount.equals(other.amount))
            return false;
        if (iban == null) {
            if (other.iban != null)
                return false;
        } else if (!iban.equals(other.iban))
            return false;
        return true;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}

