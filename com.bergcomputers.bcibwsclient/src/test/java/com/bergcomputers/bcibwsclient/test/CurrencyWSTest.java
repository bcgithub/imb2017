package com.bergcomputers.bcibwsclient.test;

import static javax.ws.rs.client.Entity.json;
import static org.valid4j.matchers.http.HttpResponseMatchers.hasStatus;
import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;

import com.bergcomputers.domain.Currency;

import junit.framework.Assert;

	public class CurrencyWSTest extends AbstractTest {
		final static String UrlBase = "http://localhost:"+"8080"+"/bcibws/rest/";
		private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
		
	    @Test
	    public void findCurrency(){
	    	//creating a new currency
	    	Currency currency = new Currency();
	    	currency.setCreationDate(new Date());
	    	currency.setExchangerate(2D);
	    	currency.setId(2L);
	    	currency.setSymbol("EUR");
	    	currency.setVersion(1);
	    	
	    	//adding the new currency.
	    	Response response = target("currency").post(json(currency));
	    	
	    	assertThat(response, hasStatus(Status.CREATED));
	    	Currency currencyResult = response.readEntity(Currency.class);
	        
	    	//getting the currency
	    	currencyResult = target("currency"+currency.getId()).get(Currency.class);
	    	
	    	//checking to see if the 2 currencies are the same
	    	Assert.assertEquals(currency.getId(), currencyResult.getId());
	    	Assert.assertEquals(currency.getVersion(), currencyResult.getVersion());
	    	Assert.assertEquals(currency.getExchangerate(), currencyResult.getExchangerate());
	    	Assert.assertTrue(currency.getCreationDate().equals(currencyResult.getCreationDate()));
	    	Assert.assertTrue(currency.getSymbol().equals(currencyResult.getSymbol()));
	    	
	    	//deleting the test currency
	    	
	    	target("currency"+currency.getId()).delete();

	    	
	    	
	    }
}
