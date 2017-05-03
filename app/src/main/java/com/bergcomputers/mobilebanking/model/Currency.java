package com.bergcomputers.mobilebanking.model;


import java.io.Serializable;


public class Currency extends BaseEntity {
    public static final String FIELD_ID="id";
    public static final String FIELD_SYMBOL="symbol";
    public static final String FIELD_EXCHANGE_RATE="exchangerate";

    private String symbol;

    private Double exchangerate;

    @Override
    public String toString() {
        return "Currency [symbol=" + symbol + ", exchangerate=" + exchangerate
                + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((exchangerate == null) ? 0 : exchangerate.hashCode());
        result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
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
        Currency other = (Currency) obj;
        if (exchangerate == null) {
            if (other.exchangerate != null)
                return false;
        } else if (!exchangerate.equals(other.exchangerate))
            return false;
        if (symbol == null) {
            if (other.symbol != null)
                return false;
        } else if (!symbol.equals(other.symbol))
            return false;
        return true;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getExchangerate() {
        return exchangerate;
    }

    public void setExchangerate(Double exchangerate) {
        this.exchangerate = exchangerate;
    }
}

