package com.bergcomputers.ejb;

import java.util.List;

import com.bergcomputers.domain.Currency;
import com.bergcomputers.domain.Role;

public interface IRoleController {

	Role find(long id);
	
	List<Role> findAll();
	
	List<Role> findRange(int startPosition, int size);
	
	void delete(long id);
	
	Role create(Role rol);
	
	Role update(Role rol);
	
	int count();
}


