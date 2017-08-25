package com.bergcomputers.ejb;

import java.util.List;

import com.bergcomputers.domain.Account;


public interface IAccountController {

	Account find(long id);
	List<Account> findAll();
	List<Account> findRange(int startPosition, int size);

	void delete(long id);
	
	Account create(Account account);
	
	Account update(Account account);
	
	List<Account> getAccountForCustomer(long customerid);
	
	int count();
}
