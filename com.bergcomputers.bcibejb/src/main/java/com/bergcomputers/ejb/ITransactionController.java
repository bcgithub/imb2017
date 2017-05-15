package com.bergcomputers.ejb;

import java.util.List;

import com.bergcomputers.domain.Transaction;

public interface ITransactionController 
{
	Transaction find(long id);
	
	List<Transaction> findAll();
	
	List<Transaction> findRange(int startPosition, int size);
	
	void delete(long id);
	
	Transaction create(Transaction transaction);
	
	Transaction update(Transaction transaction);
	
	int count();
}
