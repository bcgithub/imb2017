/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bergcomputers.bcibintegrationtest.rest;

import static javax.ws.rs.client.Entity.json;
import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;

import com.bergcomputers.domain.Currency;
import com.bergcomputers.ejb.CurrencyController;
import com.bergcomputers.ejb.ICurrencyController;
import com.bergcomputers.rest.currency.CurrencyResource;
/**
 *
 * @author Ionut
 */
public class CurrencyWSTest extends AbstractTest {

	private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
	
	private String serviceRelativePath = "currency/";
	private String jsonFormat = "application/json";
	
	private GenericType<List<Currency>> genericListType = new GenericType<List<Currency>>() {};

	private String symbol = "USD";
	private Double exchangeRate = 4.0D;
	private Date creationDate = new Date();
	
	 @Inject
	 private ICurrencyController currencyContoller;
	 
	@Deployment
	public static WebArchive createDeployment() {
		return buildArchive().addPackage(CurrencyResource.class.getPackage()).addPackage(Currency.class.getPackage())
				.addPackage(CurrencyController.class.getPackage()).addClass(CurrencyResource.class);
	}

	/**
	 * Test if we obtain the list of all currencies
	 */
	@Test
	@RunAsClient
	public void getCurrenciesTest() {
		Currency created = createCurrency();
		List<Currency> currencies = target(serviceRelativePath).accept(jsonFormat).get(genericListType);
		assertEquals(1, currencies.size());
		
		//Deleting test currency
		deleteCurrency(created.getId());

	}
	
	/**
	 * Test if we obtain the paginated list of all currencies
	 */
	@Test
	@RunAsClient
	public void getCurrenciesPaginationTest() {
		Currency created1 = createCurrency();
		Currency created2 = createCurrency();
		Map<String, Object> params = new HashMap<>();
        params.put("page", 1);
        params.put("size", 1);
        
		List<Currency> currencies = target(serviceRelativePath, params).accept(jsonFormat).get(genericListType);
		assertEquals(1, currencies.size());
		assertEquals(created1.getId(), currencies.get(0).getId());
        params.put("page", 2);
        currencies = target(serviceRelativePath, params).accept(jsonFormat).get(genericListType);
		assertEquals(1, currencies.size());
		assertEquals(created2.getId(), currencies.get(0).getId());
		
		//Deleting test currency
		deleteCurrency(created1.getId());
		deleteCurrency(created2.getId());

	}

	/**
	 * Test if a currency is created
	 */
	@Test
	@RunAsClient
	public void createCurrencyTest() {
				
		//existing currencies
		List<Currency> currencies = getCurrencies();

		//Creating test currency
		Currency currency = createCurrencyEntity();
		Response resp = target(serviceRelativePath).post(json(currency));
		Currency created = resp.readEntity(Currency.class);

		//Getting list of currencies
		List<Currency> currenciesNewList = getCurrencies();
		
		//check the list size to be increased by one
		assertEquals(currencies.size() +1, currenciesNewList.size() );
		
		assertEquals(currency.getSymbol(), created.getSymbol());
		assertEquals(currency.getExchangerate(), created.getExchangerate());
		assertEquals(currency.getCreationDate(), created.getCreationDate());
		
		//Deleting test currency
		deleteCurrency(currenciesNewList.get(0).getId());

	}
	
	/**
	 * Test if a currency can be obtained by its id
	 */
	@Test
	@RunAsClient
	public void getCurrencyTest() {
				
		//Creating test currency
		Currency currency = createCurrencyEntity();
		Response resp = target(serviceRelativePath).post(json(currency));
		Currency created = resp.readEntity(Currency.class);
		
		resp = target(serviceRelativePath+created.getId()).get();
		Currency obtained = resp.readEntity(Currency.class);
		
		assertEquals(obtained.getId(), created.getId());
		assertEquals(obtained.getSymbol(), created.getSymbol());
		assertEquals(obtained.getExchangerate(), created.getExchangerate());
		assertEquals(obtained.getCreationDate(), created.getCreationDate());
		
		//Deleting test currency
		deleteCurrency(created.getId());

	}
	
	/**
	 * 
	 * Test if a currency is updated 
	 */
	@Test
	@RunAsClient
	public void updateCurrencyTest() {
		Double newRate = 3.0d;
		String newSymbol = "EUR";
		Date newCreation = new Date();
		
		//Creating test currency
		Currency currency = createCurrency();

		currency.setExchangerate(newRate);
		currency.setSymbol(newSymbol);
		currency.setCreationDate(newCreation);
		
		Response resp = target(serviceRelativePath).put(json(currency));
		Currency updated = resp.readEntity(Currency.class);
		
		assertEquals(currency.getId(), updated.getId());
		assertEquals(newSymbol, updated.getSymbol());
		assertEquals(newRate, updated.getExchangerate());
		assertEquals(newCreation, updated.getCreationDate());
		
		//Deleting test currency
		deleteCurrency(currency.getId());

	}
	
	/**
	 *	Test if a currency is deleted 
	 * 
	 */
	@Test
	@RunAsClient
	public void deleteCurrencyTest() {
		// Creating test currency
		createCurrency();
		
		// existing currencies
		List<Currency> currencies = getCurrencies();
		
		
		//delete test currency
		target(serviceRelativePath + currencies.get(0).getId()).delete();
		
		// new currencies list
		List<Currency> currenciesNewList = getCurrencies();
		
		//check the list size to be decrease by one
		assertEquals(currencies.size() - 1, currenciesNewList.size() );

	}
	
	/**
	 * 
	 * @return the created entity pojo
	 */
	private Currency createCurrencyEntity(){
		Currency currency = new Currency();
		currency.setExchangerate(exchangeRate);
		currency.setSymbol(symbol);
		currency.setCreationDate(creationDate);
		return currency;
	}
	
	/**
	 * 
	 * @return the created entity in the database
	 */
	private Currency createCurrency(){
		Currency currency = createCurrencyEntity();
		Response resp = target(serviceRelativePath).post(json(currency));
		Currency created = resp.readEntity(Currency.class);
		return created;
	}
	
	/**
	 * 
	 * @return list of existing currencies in the database
	 */
	private List<Currency> getCurrencies(){
		return target(serviceRelativePath).accept(jsonFormat).get(genericListType);
	}
	
	/**
	 * Deletes the specified entity from db
	 * 
	 * @param id
	 */
	private void deleteCurrency(Long id){
		target(serviceRelativePath + id).delete();
	}
}
