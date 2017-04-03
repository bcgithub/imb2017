package com.bergcomputers.bcibpersistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.bergcomputers.domain.Currency;

public class CurrencyEntityValidatorsTest {
	private static EntityManagerFactory emf;

    private EntityManager em;

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
    public void nullValidation() {

        try {
           Currency currency = new Currency();
           currency.setExchangerate(1.2);
           currency.setCreationDate(new Date());
           em.persist(currency);
            //em.getTransaction().commit();
            fail("Expected ConstraintViolationException wasn't thrown.");
        }
        catch (ConstraintViolationException e) {
            assertEquals(1, e.getConstraintViolations().size());
            ConstraintViolation<?> violation =
                e.getConstraintViolations().iterator().next();

            assertEquals("symbol", violation.getPropertyPath().toString());
            assertEquals(
                NotNull.class,
                violation.getConstraintDescriptor().getAnnotation().annotationType());
        }
    }
}
