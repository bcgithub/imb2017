package com.bergcomputers.ejb;

import java.util.List;

import com.bergcomputers.domain.Device;


public interface IDeviceController {

	Device find(long id);
	
	List<Device> findAll();
	
	List<Device> findRange(int startPosition, int size);
	
	void delete(long id);
	
	Device create(Device device);
	
	Device update(Device device);
	
	int count();
}
