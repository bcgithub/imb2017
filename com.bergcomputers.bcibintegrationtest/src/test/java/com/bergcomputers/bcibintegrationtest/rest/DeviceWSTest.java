package com.bergcomputers.bcibintegrationtest.rest;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import static javax.ws.rs.client.Entity.json;
import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;

import com.bergcomputers.domain.Device;
import com.bergcomputers.ejb.DeviceControler;
import com.bergcomputers.ejb.IDeviceController;
import com.bergcomputers.rest.device.DeviceResource;
/**
 *
 * @author Ionut
 */
public class DeviceWSTest extends AbstractTest {

	private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
	
	private String serviceRelativePath = "device/";
	private String jsonFormat = "application/json";
	
	private GenericType<List<Device>> genericListType = new GenericType<List<Device>>() {};

	private String name = "DeviceName";
	private long deviceId = 54;
	private Date creationDate = new Date();
	
	 @Inject
	 private IDeviceController deviceController;
	 
	@Deployment
	public static WebArchive createDeployment() {
		return buildArchive().addPackage(DeviceResource.class.getPackage()).addPackage(Device.class.getPackage())
				.addPackage(DeviceControler.class.getPackage()).addClass(DeviceResource.class);
	}

	/**
	 * Test if we obtain the list of all devices
	 */
	@Test
	@RunAsClient
	public void getDevicesTest() {
		Device created = createDevice();
		List<Device> devices = target(serviceRelativePath).accept(jsonFormat).get(genericListType);
		assertEquals(1, devices.size());
		
		//Deleting test Device
		deleteDevice(created.getId());

	}
	
	/**
	 * Test if we obtain the paginated list of all currencies
	 */
	@Test
	@RunAsClient
	public void getDevicesPaginationTest() {
		Device created1 = createDevice();
		Device created2 = createDevice();
		Map<String, Object> params = new HashMap<>();
        params.put("page", 1);
        params.put("size", 1);
        
		List<Device> devices = target(serviceRelativePath, params).accept(jsonFormat).get(genericListType);
		assertEquals(1, devices.size());
		assertEquals(created1.getId(), devices.get(0).getId());
        params.put("page", 2);
        devices = target(serviceRelativePath, params).accept(jsonFormat).get(genericListType);
		assertEquals(1, devices.size());
		assertEquals(created2.getId(), devices.get(0).getId());
		
		//Deleting test device
		deleteDevice(created1.getId());
		deleteDevice(created2.getId());

	}

	/**
	 * Test if a Device is created
	 */
	@Test
	@RunAsClient
	public void createDeviceTest() {
				
		//existing devices
		List<Device> devices = getDevices();

		//Creating test device
		Device device = createDeviceEntity();
		Response resp = target(serviceRelativePath).post(json(device));
		Device created = resp.readEntity(Device.class);

		//Getting list of currencies
		List<Device> devicesNewList = getDevices();
		
		//check the list size to be increased by one
		assertEquals(devices.size() +1, devicesNewList.size() );
		
		assertEquals(device.getName(), created.getName());
		assertEquals(device.getDeviceId(), created.getDeviceId());
		assertEquals(device.getCreationDate(), created.getCreationDate());
		
		//Deleting test Device
		deleteDevice(devicesNewList.get(0).getId());

	}
	
	/**
	 * Test if a device can be obtained by its id
	 */
	@Test
	@RunAsClient
	public void getDeviceTest() {
				
		//Creating test device
		Device device = createDeviceEntity();
		Response resp = target(serviceRelativePath).post(json(device));
		Device created = resp.readEntity(Device.class);
		
		resp = target(serviceRelativePath+created.getId()).get();
		Device obtained = resp.readEntity(Device.class);
		
		assertEquals(obtained.getId(), created.getId());
		assertEquals(obtained.getName(), created.getName());
		assertEquals(obtained.getDeviceId(), created.getDeviceId());
		assertEquals(obtained.getCreationDate(), created.getCreationDate());
		
		//Deleting test Device
		deleteDevice(created.getId());

	}
	
	/**
	 * 
	 * Test if a Device is updated 
	 */
	@Test
	@RunAsClient
	public void updateDeviceTest() {
		String newName = "NewDeviceName";
		long newDeviceId = 21;
		Date newCreation = new Date();
		
		//Creating test Device
		Device device = createDevice();

		device.setName(newName);
		device.setDeviceId(newDeviceId);
		device.setCreationDate(newCreation);
		
		Response resp = target(serviceRelativePath).put(json(device));
		Device updated = resp.readEntity(Device.class);
		
		assertEquals(device.getId(), updated.getId());
		assertEquals(newName, updated.getName());
		assertEquals(newDeviceId, updated.getDeviceId());
		assertEquals(newCreation, updated.getCreationDate());
		
		//Deleting test Device
		deleteDevice(device.getId());

	}
//DE aici modific	
	/**
	 *	Test if a device is deleted 
	 * 
	 */
	@Test
	@RunAsClient
	public void deleteDeviceTest() {
		// Creating test device
		createDevice();
		
		// existing devices
		List<Device> devices = getDevices();
		
		
		//delete test device
		target(serviceRelativePath + devices.get(0).getId()).delete();
		
		// new currencies list
		List<Device> devicesNewList = getDevices();
		
		//check the list size to be decrease by one
		assertEquals(devices.size() - 1, devicesNewList.size() );

	}
	
	/**
	 * 
	 * @return the created entity pojo
	 */
	private Device createDeviceEntity(){
		Device device = new Device();
		device.setName(name);
		device.setDeviceId(deviceId);
		device.setCreationDate(creationDate);
		return device;
	}
	
	/**
	 * 
	 * @return the created entity in the database
	 */
	private Device createDevice(){
		Device Device = createDeviceEntity();
		Response resp = target(serviceRelativePath).post(json(Device));
		Device created = resp.readEntity(Device.class);
		return created;
	}
	
	/**
	 * 
	 * @return list of existing currencies in the database
	 */
	private List<Device> getDevices(){
		List<Device> devices = target(serviceRelativePath).accept(jsonFormat).get(genericListType);
		return devices;
	}
	
	/**
	 * Deletes the specified entity from db
	 * 
	 * @param id
	 */
	private void deleteDevice(Long id){
		target(serviceRelativePath + id).delete();
	}
}
