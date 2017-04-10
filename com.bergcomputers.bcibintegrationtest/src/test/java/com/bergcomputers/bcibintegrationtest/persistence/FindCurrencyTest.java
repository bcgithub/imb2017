package com.bergcomputers.bcibintegrationtest.persistence;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.CleanupUsingScript;
import org.jboss.arquillian.persistence.PersistenceTest;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.arquillian.persistence.TransactionMode;
import org.jboss.arquillian.persistence.Transactional;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.bergcomputers.domain.Currency;


@RunWith(Arquillian.class)
@PersistenceTest
@Transactional(TransactionMode.DISABLED)
public class FindCurrencyTest {
	
	private String symbol = "USD";
	private Double exchangeRate = 4.0d;
	
	@Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
            .addPackage(Currency.class.getPackage())
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

	@PersistenceContext
    EntityManager em;

    @Inject
    UserTransaction utx;

    @Before
    public void preparePersistenceTest() throws Exception {
        clearData();
        insertData();
        startTransaction();
    }
    //@Transactional(TransactionMode.DISABLED)
    private void clearData() throws Exception {
        utx.begin();
        em.joinTransaction();
        System.out.println("Dumping old records...");
        em.createQuery("delete from Currency").executeUpdate();
        utx.commit();
    }

    private void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();
        System.out.println("Inserting records...");
        Currency u1 = new Currency();
        u1.setSymbol(symbol);
        u1.setExchangerate(exchangeRate);
        u1.setCreationDate(new Date());
        em.persist(u1);
        utx.commit();
        // clear the persistence context (first-level cache)
        em.clear();
    }

    private void startTransaction() throws Exception {
        utx.begin();
        em.joinTransaction();
    }

    @CleanupUsingScript(phase = TestExecutionPhase.BEFORE,value="datasets/cleanup-CurrencyTest.sql")
    @Test
    public void shouldFindAllCurrenciesJpqlQuery() throws Exception {
        // given
        String fetchingAllRecordsInJpql = "select u from Currency u order by u.id";

        // when
        System.out.println("Selecting (using JPQL)...");
        List<Currency> currencies = em.createQuery(fetchingAllRecordsInJpql, Currency.class).getResultList();

        // then
        System.out.println("Found " + currencies.size() + " currencies (using JPQL):");
        Assert.assertEquals(symbol, currencies.get(0).getSymbol());
        Assert.assertEquals(exchangeRate, currencies.get(0).getExchangerate());

    }

    @After
    public void commitTransaction() throws Exception {
        utx.commit();
    }
}
