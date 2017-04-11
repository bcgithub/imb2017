package com.bergcomputers.ejb;

import java.util.List;

import com.bergcomputers.domain.Beneficiary;
import com.bergcomputers.domain.Currency;

public interface IBeneficiaryController {

	
	Beneficiary find(long id);
	
	List<Beneficiary> findAll();
	
	List<Beneficiary> findRange(int startPosition, int size);
	
}
