package com.bergcomputers.ejb;

import java.util.List;

import com.bergcomputers.domain.Customer;

public interface  ICustomerController {

	Customer find(long id);
	
	List<Customer> findAll();
	
	List<Customer> findRange(int startPosition, int size);
	
	void delete(long customerid);
	
	Customer create(Customer customer);
	
	Customer update(Customer customer);
	
	int count();
	
		
}
