package com.bergcomputers.ejb;

import java.util.List;

import com.bergcomputers.domain.Currency;

public interface ICurrencyController {

	Currency find(long id);
	
	List<Currency> findAll();
	
	List<Currency> findRange(int startPosition, int size);
	
	void delete(long id);
	
	Currency create(Currency currency);
	
	Currency update(Currency currency);
	
	int count();
}
