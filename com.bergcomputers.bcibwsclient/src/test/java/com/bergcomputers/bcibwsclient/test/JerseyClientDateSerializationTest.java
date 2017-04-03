package com.bergcomputers.bcibwsclient.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.bergcomputers.domain.Currency;

public class JerseyClientDateSerializationTest extends AbstractTest{

	final static String UrlBase = "http://localhost:" + "8080"
			+ "/bcibws/rest/";

	private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
	//private static ObjectMapper om = null;
	@BeforeClass
	public static void setupClass() {
	}

	@AfterClass
	public static void cleanup() {
	}

	@Test
	public void createAccount() {
			// creating a new currency to associate with the account
		Currency currency = new Currency();
		currency.setId(1L);
		currency.setSymbol("USD");
		currency.setExchangerate(1.0);

		/*String res;
		try {
			res = om.writeValueAsString(acc);
			System.out.println(res);

		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		//create new currency
		Response resp =target("currency").post(Entity.json(currency));
		System.out.println("-----");
		Currency currencyResult =resp.readEntity(Currency.class);

		// testing to see if the account is the same
		Assert.assertEquals(currency.getSymbol(), currencyResult.getSymbol());
		Assert.assertEquals(currency.getExchangerate(), currencyResult.getExchangerate());
		Assert.assertEquals(currency.getId(), currencyResult.getId());
		

		// deleting the account
		System.out.println("Deleting test currency:");
		System.out.println(currencyResult.getId());
		target("currency/" + currencyResult.getId()).delete();
		System.out.println("-----");

	}


}
