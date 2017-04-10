package com.bergcomputers.ejb;

import java.util.List;

import com.bergcomputers.domain.Currency;
import com.bergcomputers.domain.Customer;

public interface  ICustomerController {

	Customer find(long id);
	List<Customer> findAll();
	
	
}
