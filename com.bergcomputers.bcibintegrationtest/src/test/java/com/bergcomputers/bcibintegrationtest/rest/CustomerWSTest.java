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

import com.bergcomputers.domain.Currency;
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
	private String firstName="firstName";
	private String lastName="lastName";
	private String login="Login";
	private String password="Password";
	
	private Date creationDate = new Date();
	
	 @Inject
	 private ICustomerController customerContoller;
	 
	@Deployment
	public static WebArchive createDeployment() 
	{
		return buildArchive().addPackage(CustomerResource.class.getPackage()).addPackage(Customer.class.getPackage())
				.addPackage(CustomerController.class.getPackage()).addClass(CustomerResource.class);
	}
	
	public void getCustomersTest() 
	{
		Customer created = createCustomer();
		List<Customer> customers = target(serviceRelativePath).accept(jsonFormat).get(genericListType);
		assertEquals(1, customers.size());
		deleteCustomer(created.getId());
	}
	
/*	@Test
	@RunAsClient
	public void getCustomersPaginationTest() 
	{
		Customer created1 = createCustomer();
		Customer created2 = createCustomer();
		Map<String, Object> params = new HashMap<>();
        params.put("page", 1);
        params.put("size", 1);
        
		List<Customer> customers = target(serviceRelativePath, params).accept(jsonFormat).get(genericListType);
		assertEquals(1, customers.size());
		assertEquals(created1.getId(), customers.get(0).getId());
        params.put("page", 2);
        customers = target(serviceRelativePath, params).accept(jsonFormat).get(genericListType);
		assertEquals(1, customers.size());
		assertEquals(created2.getId(), customers.get(0).getId());
		
		//Deleting test currency
		deleteCustomer(created1.getId());
		deleteCustomer(created2.getId());

	}*/
	
	@Test
	@RunAsClient
	public void createCustomerTest() 
	{
		
		//existing currencies
		List<Customer> customers = getCustomers();

		//Creating test currency
		Customer customer = createCustomer();
		Response resp = target(serviceRelativePath).post(json(customer));
		Customer created = resp.readEntity(Customer.class);

		//Getting list of currencies
		List<Customer> customersNewList = getCustomers();
		
		//check the list size to be increased by one
		assertEquals(customers.size() +2, customersNewList.size() );
		
		assertEquals(customer.getFirstName(), created.getFirstName());
		assertEquals(customer.getLastName(), created.getLastName());
		assertEquals(customer.getLogin(), created.getLogin());
		assertEquals(customer.getPassword(), created.getPassword());
		assertEquals(customer.getCreationDate(), created.getCreationDate());
		
		//Deleting test currency
		deleteCustomer(customersNewList.get(0).getId());

	}
	
	@Test
	@RunAsClient
	public void getCustomerTest() {
				
		//Creating test customer
		Customer customer = createCustomer();
		Response resp = target(serviceRelativePath).post(json(customer));
		Customer created = resp.readEntity(Customer.class);
		
		resp = target(serviceRelativePath+created.getId()).get();
		Customer obtained = resp.readEntity(Customer.class);
		
		assertEquals(obtained.getId(), created.getId());
		assertEquals(customer.getFirstName(), created.getFirstName());
		assertEquals(customer.getLastName(), created.getLastName());
		assertEquals(customer.getLogin(), created.getLogin());
		assertEquals(customer.getPassword(), created.getPassword());
		assertEquals(obtained.getCreationDate(), created.getCreationDate());
		
		//Deleting test customer
		deleteCustomer(created.getId());
	}
	
	@Test
	@RunAsClient
	public void updateCustomerTest() 
	{
		Date newCreation = new Date();
		
		//Creating test currency
		Customer customer = createCustomer();

		customer.setFirstName(firstName);
		customer.setLastName(lastName);
		customer.setLogin(login);
		customer.setPassword(password);
		customer.setCreationDate(newCreation);
		
		Response resp = target(serviceRelativePath).put(json(customer));
		Customer updated = resp.readEntity(Customer.class);
		
		assertEquals(customer.getId(), updated.getId());
		assertEquals(firstName, updated.getFirstName());
		assertEquals(lastName, updated.getLastName());
		assertEquals(login, updated.getLogin());
		assertEquals(password, updated.getPassword());
		assertEquals(newCreation, updated.getCreationDate());
		
		//Deleting test currency
		deleteCustomer(customer.getId());

	}
		
	@Test
	@RunAsClient
	public void deleteCustomerTest() 
	{
		// Creating test currency
		createCustomer();
		
		// existing currencies
		List<Customer> customers = getCustomers();
		
		
		//delete test currency
		target(serviceRelativePath + customers.get(0).getId()).delete();
		
		// new currencies list
		List<Customer> customersNewList = getCustomers();
		
		//check the list size to be decrease by one
		assertEquals(customers.size() - 1, customersNewList.size() );

	}
	
	private Customer createCustomerEntity(){
		Customer customer = new Customer();
		customer.setFirstName(firstName);
		customer.setLastName(lastName);
		customer.setLogin(login);
		customer.setPassword(password);
		customer.setCreationDate(creationDate);
		return customer;
	}
	
	
	
	private Customer createCustomer(){
		Customer customer = createCustomerEntity();
		Response resp = target(serviceRelativePath).post(json(customer));
		Customer created = resp.readEntity(Customer.class);
		return created;
	}
	
	private List<Customer> getCustomers(){
		return target(serviceRelativePath).accept(jsonFormat).get(genericListType);
	}
	
	private void deleteCustomer(Long id)
	{
		target(serviceRelativePath + id).delete();
	}
	
	private Customer createOtherCustomerEntity(){
		Customer customer = new Customer();
		customer.setFirstName("OtherCustomer");
		customer.setLastName(lastName);
		customer.setLogin(login);
		customer.setPassword(password);
		customer.setCreationDate(creationDate);
		return customer;
	}	
	
	private Customer createOtherCustomer(){
		Customer customer = createOtherCustomerEntity();
		Response resp = target(serviceRelativePath).post(json(customer));
		Customer created = resp.readEntity(Customer.class);
		return created;
	}
	
	@Test
//	@RunAsClient
	public void getCustomersPaginationTest() 
	{
		Customer created1 = createCustomer();
		Customer created2 = createOtherCustomer();
		Map<String, Object> params = new HashMap<>();
        params.put("page", 1);
        params.put("size", 1);
        
		List<Customer> customers = target(serviceRelativePath, params).accept(jsonFormat).get(genericListType);
		assertEquals(1, customers.size());
		assertEquals(created1.getFirstName(), customers.get(0).getFirstName());
        params.put("page", 2);
        customers = target(serviceRelativePath, params).accept(jsonFormat).get(genericListType);
		assertEquals(1, customers.size());
		assertEquals(created2.getFirstName(), customers.get(0).getFirstName());
		
		//Deleting test currency
		deleteCustomer(created1.getId());
		deleteCustomer(created2.getId());

	}
}