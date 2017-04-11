package com.bergcomputers.ejb;

import java.util.List;

import com.bergcomputers.domain.Device;

public interface IDeviceController {

	
	List<Device> findRange(int startPosition, int size);
}
