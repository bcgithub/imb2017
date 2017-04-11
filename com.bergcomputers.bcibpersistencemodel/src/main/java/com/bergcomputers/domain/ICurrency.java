package com.bergcomputers.domain;

public interface ICurrency {
	public static final String symbol = new String("symbol");
	public static final String exchangerate = new String("exchangeRate");
	
	String getSymbol();
	void setSymbol(String symbol);
	Double getExchangerate();
	void setExchangerate(Double exchangerate);
}
