package com.bergcomputers.bcibintegrationtest.rest;

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

import com.bergcomputers.domain.Customer;
import com.bergcomputers.ejb.CustomerController;
import com.bergcomputers.ejb.ICustomerController;
import com.bergcomputers.rest.customer.CustomerResource;



public class CustomerWSTest extends AbstractTest {

private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
	
	private String serviceRelativePath = "customer/";
	private String jsonFormat = "application/json";
	
	private GenericType<List<Customer>> genericListType = new GenericType<List<Customer>>() {};

	private String role;
	private String firstName;
	private String lastName;
	private String login;
	private String password;
	
	private Date creationDate = new Date();
	
	 @Inject
	 private ICustomerController customerContoller;
	 
	@Deployment
	public static WebArchive createDeployment() {
		return buildArchive().addPackage(CustomerResource.class.getPackage()).addPackage(Customer.class.getPackage())
				.addPackage(CustomerController.class.getPackage()).addClass(CustomerResource.class);
	}
}