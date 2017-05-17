package com.bergcomputers.domain;

public interface IDevice {

	String Name = null;
	long id = 0;

	public abstract String getName();

	public abstract void setName(String name);

	public abstract long getDeviceId();

	public abstract void setDeviceId(long deviceId);

	public abstract Customer getCustomer();

	public abstract void setCustomer(Customer customer);

}