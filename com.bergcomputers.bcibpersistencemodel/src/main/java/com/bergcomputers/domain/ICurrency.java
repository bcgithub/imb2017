package com.bergcomputers.domain;

public interface ICurrency {
	String symbol = "symbol";
	String exchangerate = "exchangerate";
	String getSymbol();
	void setSymbol(String symbol);
	Double getExchangerate();
	void setExchangerate(Double exchangerate);
}
