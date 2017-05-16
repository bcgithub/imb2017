package com.bergcomputers.mobilebanking.model;

import java.io.Serializable;

/**
 * Entity implementation class for Entity: Beneficiary
 *
 */


public class Beneficiary extends BaseEntity implements Serializable, IBeneficiary {


    public static final String FIELD_ID="id";
    public static final String FIELD_IBAN="iban";
    public static final String FIELD_NAME="name";
    public static final String FIELD_DETAILS="details";
    public static final String FIELD_ACCOUNTHOLDER="accountholder";

    public final static String findAll = "com.bergcomputers.domain.beneficiary.findAll";


    private String iban;


    private String name;


    private String details;

    private String accountholder;

    public Beneficiary()
    {
        super();
    }

    public String getIban()
    {
        return this.iban;
    }

    public void setIban(String iban)
    {
        this.iban=iban;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name=name;
    }

    public String getDetails()
    {
        return this.details;
    }

    public void setDetails(String details)
    {
        this.details=details;
    }

    public String getAccountHolder()
    {
        return this.accountholder;
    }

    public void setAccountHolder(String accountholder)
    {
        this.accountholder=accountholder;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((accountholder == null) ? 0 : accountholder.hashCode());
        result = prime * result + ((details == null) ? 0 : details.hashCode());
        result = prime * result + ((iban == null) ? 0 : iban.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        Beneficiary other = (Beneficiary) obj;
        if (accountholder == null) {
            if (other.accountholder != null)
                return false;
        } else if (!accountholder.equals(other.accountholder))
            return false;
        if (details == null) {
            if (other.details != null)
                return false;
        } else if (!details.equals(other.details))
            return false;
        if (iban == null) {
            if (other.iban != null)
                return false;
        } else if (!iban.equals(other.iban))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }


}
