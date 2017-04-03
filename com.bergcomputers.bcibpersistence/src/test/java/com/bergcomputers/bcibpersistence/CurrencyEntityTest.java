package com.bergcomputers.bcibpersistence;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.bergcomputers.domain.Currency;

public class CurrencyEntityTest {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Test
	public void testEquals(){
		Currency currency = new Currency();
		currency.setExchangerate(1.2);
		currency.setSymbol("$");

		Currency anotherUSD = new Currency();
		anotherUSD.setExchangerate(1.2);
		anotherUSD.setSymbol("$");

		assertEquals(anotherUSD,currency);
	}

	@Test
	public void testSettersAndToString() throws ParseException{
		Long id = new Long(1);
		Double exchangeRate = 1.2;
		Date creationDate = sdf.parse("2014-03-29 10:15:00");
		
		Currency currency = new Currency();
		currency.setId(id);
		currency.setExchangerate(1.2);
		currency.setSymbol("$");
		currency.setCreationDate(creationDate);

		 String textRepr = "Currency [symbol=" + currency.getSymbol() + ", exchangerate=" + currency.getExchangerate() + "]";
		assertEquals(textRepr,currency.toString());
	}
}
