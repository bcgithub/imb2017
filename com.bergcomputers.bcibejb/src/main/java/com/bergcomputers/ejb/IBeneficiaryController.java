package com.bergcomputers.ejb;

import java.util.List;

import com.bergcomputers.domain.Beneficiary;
import com.bergcomputers.domain.Currency;
import com.bergcomputers.domain.Role;

public interface IBeneficiaryController {

	
	Beneficiary find(long id);
	
	List<Beneficiary> findAll();
	
	List<Beneficiary> findRange(int startPosition, int size);
	
	void delete(long id);
	
	Beneficiary create(Beneficiary bnf);
	
	Beneficiary update(Beneficiary bnf);
	
	int count();
}
