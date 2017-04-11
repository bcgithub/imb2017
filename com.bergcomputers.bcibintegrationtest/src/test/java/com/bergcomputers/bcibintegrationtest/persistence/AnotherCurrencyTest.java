package com.bergcomputers.bcibintegrationtest.persistence;

import java.util.Date;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.CleanupUsingScript;
import org.jboss.arquillian.persistence.PersistenceTest;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.arquillian.persistence.TransactionMode;
import org.jboss.arquillian.persistence.Transactional;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.bergcomputers.domain.Currency;


@RunWith(Arquillian.class)
@PersistenceTest
@Transactional(TransactionMode.DISABLED)
public class AnotherCurrencyTest {
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
    public void setup() throws Exception {
    }

    @Test
	@UsingDataSet("datasets/OtherTwoCurrencies.xml")
	@ShouldMatchDataSet(value="datasets/OtherTwoCurrencies_save.xml", excludeColumns = { "id","creationDate","currencyid"})
	@Transactional(TransactionMode.COMMIT)
    @CleanupUsingScript(phase = TestExecutionPhase.BEFORE,value="datasets/cleanup-CurrencyTest.sql")
	public void saveCurrency() {
    	 Currency u1 = new Currency();
    	 u1.setId(8L);
         u1.setSymbol("USD");
         u1.setExchangerate(1.2);
         u1.setCreationDate(new Date());
         try{
         em.persist(u1);
         }catch(Throwable ex){
        	 System.out.println("ionut:"+ex.getCause().getMessage());
         }
	}

    @After
    public void cleanup() throws Exception {
    }
}
