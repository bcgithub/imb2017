package com.bergcomputers.bcibpersistence;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.bergcomputers.domain.Currency;

public class CurrencyEntityTest {

	private static EntityManagerFactory emf;
    private EntityManager em;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @BeforeClass
    public static void createEntityManagerFactory() {
        emf = Persistence.createEntityManagerFactory("testPU");
    }

    @AfterClass
    public static void closeEntityManagerFactory() {
        emf.close();
    }

    @Before
    public void beginTransaction() {
        em = emf.createEntityManager();
        em.getTransaction().begin();
    }

    @After
    public void rollbackTransaction() {

        if (em.getTransaction().isActive())
            em.getTransaction().rollback();

        if (em.isOpen())
            em.close();
    }
	
	@Test
    public void createCurrency() {

           Currency currency = new Currency();
           currency.setSymbol("USD");
           currency.setExchangerate(1.2);
           currency.setCreationDate(new Date());
           em.persist(currency);
           em.flush();
           
           Currency created= em.find(Currency.class, 1L);   
           
        assertEquals(new Long(1L), created.getId());
		assertEquals(currency.getSymbol(), created.getSymbol());
		assertEquals(currency.getExchangerate(), created.getExchangerate());
		assertEquals(currency.getCreationDate(), created.getCreationDate());
    }
	
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
