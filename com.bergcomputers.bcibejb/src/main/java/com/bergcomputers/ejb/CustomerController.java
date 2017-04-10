package com.bergcomputers.ejb;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.bergcomputers.domain.Currency;
import com.bergcomputers.domain.Customer;

public class CustomerController extends AbstractController<Currency, Long>  implements ICustomerController {
	
	
	

	public CustomerController(Class<Currency> entityClass) {
		super(entityClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Customer find(long id) {
		return getEntityManager().find(Customer.class, id);
	}

	@Override
	protected EntityManager getEntityManager() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Customer> findAll() {
		return getEntityManager().createQuery("select c from Customer c").getResultList();
	}
	
	

}
