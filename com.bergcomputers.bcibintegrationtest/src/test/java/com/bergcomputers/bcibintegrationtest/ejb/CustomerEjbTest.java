package com.bergcomputers.bcibintegrationtest.ejb;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.CleanupStrategy;
import org.jboss.arquillian.persistence.PersistenceTest;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.arquillian.persistence.TransactionMode;
import org.jboss.arquillian.persistence.Transactional;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.bergcomputers.domain.Currency;
import com.bergcomputers.domain.Customer;
import com.bergcomputers.ejb.CustomerController;
import com.bergcomputers.ejb.ICustomerController;


@RunWith(Arquillian.class)
@PersistenceTest
@Transactional(TransactionMode.ROLLBACK)

public class CustomerEjbTest 
{
	@EJB
	private ICustomerController customerController;

	@Deployment
    public static Archive<?> createDeployment() 
	{
        return ShrinkWrap.create(WebArchive.class, "test.war")
            .addPackage(Customer.class.getPackage())
            .addPackage(CustomerController.class.getPackage())
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}
	

@Before
public void preparePersistenceTest() throws Exception {
}
@After
public void commitTransaction() throws Exception {
}

@Test
@UsingDataSet("datasets/TwoCurrencies.xml")
@Cleanup(phase = TestExecutionPhase.BEFORE, strategy = CleanupStrategy.USED_TABLES_ONLY)
public void findOneCustomerUsingJpqlQuery() throws Exception {
	Customer result = customerController.find(1L);
	    assertNotNull(result);
	assertEquals(toString(), result.getFirstName());
	assertEquals(toString(), result.getLastName());
	assertEquals(toString(), result.getLogin());
	assertEquals(toString(), result.getPassword());
}


}
