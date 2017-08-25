package com.bergcomputers.bcibintegrationtest.rest;

import static javax.ws.rs.client.Entity.json;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

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

import com.bergcomputers.domain.Account;
import com.bergcomputers.domain.Currency;
import com.bergcomputers.domain.Customer;
import com.bergcomputers.domain.Role;
import com.bergcomputers.ejb.AccountController;
import com.bergcomputers.ejb.IAccountController;
import com.bergcomputers.ejb.ICurrencyController;
import com.bergcomputers.ejb.ICustomerController;
import com.bergcomputers.ejb.IRoleController;
import com.bergcomputers.rest.account.AccountResource;

public class AccountWSTest extends AbstractTest{

private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
	
	private String serviceRelativePath = "account/";
	private String jsonFormat = "application/json";
	
	private GenericType<List<Account>> genericListType = new GenericType<List<Account>>() {};

	private Date creationDate = new Date();
	private String iban="IBAN1234DE";
	private Double amount=100.0;
	
	 @Inject
	 private IAccountController accountContoller;
	 @Inject
	 private ICustomerController customerController;
	 @Inject
	 private ICurrencyController currencyController;
	 @Inject
	 private IRoleController roleController;
	 
	 @Deployment
		public static WebArchive createDeployment() {
			return buildArchive().addPackage(AccountResource.class.getPackage()).addPackage(Account.class.getPackage())
					.addPackage(AccountController.class.getPackage()).addClass(AccountResource.class);
		}
		/**
		 * Test if we obtain the list of all accounts
		 */
		@Test
		@RunAsClient
		public void getAccountsTest() {
			Account created = createAccount();
			List<Account> accounts = target(serviceRelativePath).accept(jsonFormat).get(genericListType);
			assertEquals(1, accounts.size());
			
			//Deleting test currency
			deleteAccount(created.getId());

		}
		/**
		 * Test if we obtain the paginated list of all accounts
		 */
		@Test
		@RunAsClient
		public void getAccountsPaginationTest() {
			Account created1 = createAccount();
			Account created2 = createAccount();
			Map<String, Object> params = new HashMap<>();
	        params.put("page", 1);
	        params.put("size", 1);
	        
			List<Account> accounts = target(serviceRelativePath, params).accept(jsonFormat).get(genericListType);
			assertEquals(1, accounts.size());
			assertEquals(created1.getId(), accounts.get(0).getId());
	        params.put("page", 2);
	        accounts = target(serviceRelativePath, params).accept(jsonFormat).get(genericListType);
			assertEquals(1, accounts.size());
			assertEquals(created2.getId(), accounts.get(0).getId());
			
			//Deleting test currency
			deleteAccount(created1.getId());
			deleteAccount(created2.getId());

		}
		@Test
//		@RunAsClient
		public void getAccountsNoPaginationTest() {
			Account created1 = createAccount();
		//	Account created2 = createAccount();
			Map<String, Object> params = new HashMap<>();
	        params.put("page",-1);
	        params.put("size", 1);
	        try{
			List<Account> accounts = target(serviceRelativePath, params).accept(jsonFormat).get(genericListType);
			assertEquals(1, accounts.size());
			assertEquals(created1.getId(), accounts.get(0).getId());
	        }finally{
			//Deleting test currency
			deleteAccount(created1.getId());
	        }

		}
		@Test
		@RunAsClient
		public void getAccountsNullPaginationTest() {
			Account created1 = createAccount();
		//	Account created2 = createAccount();
			Map<String, Object> params = new HashMap<>();
	        params.put("page",null);
	        params.put("size", 1);
	        try{
			List<Account> accounts = target(serviceRelativePath, params).accept(jsonFormat).get(genericListType);
			assertEquals(1, accounts.size());
			assertEquals(created1.getId(), accounts.get(0).getId());
	        }finally{
			//Deleting test currency
			deleteAccount(created1.getId());
	        }

		}
		@Test
	//	@RunAsClient
		public void getAccountsNoSizePaginationTest() {
			Account created1 = createAccount();
		//	Account created2 = createAccount();
			Map<String, Object> params = new HashMap<>();
	        params.put("page", 1);
	        params.put("size", -1);
	        
			List<Account> accounts = target(serviceRelativePath, params).accept(jsonFormat).get(genericListType);
			try{
			assertEquals(1, accounts.size());
			assertEquals(created1.getId(), accounts.get(0).getId());
			}finally{
			//Deleting test currency
			deleteAccount(created1.getId());

			}

		}
		@Test
		//	@RunAsClient
			public void getAccountsNullSizePaginationTest() {
				Account created1 = createAccount();
			//	Account created2 = createAccount();
				Map<String, Object> params = new HashMap<>();
		        params.put("page", 1);
		       // params.put("size", null);
		        
				List<Account> accounts = target(serviceRelativePath, params).accept(jsonFormat).get(genericListType);
				try{
				assertEquals(1, accounts.size());
				assertEquals(created1.getId(), accounts.get(0).getId());
				}finally{
				//Deleting test currency
				deleteAccount(created1.getId());

				}

			}
		
		/**
		 * Test if an account is created
		 */
		@Test
		@RunAsClient
		public void createAccountTest() {
					
			//existing currencies
			List<Account> accounts = getAccounts();

			//Creating test account
			Account account = createAccountEntity();
			Response resp = target(serviceRelativePath).post(json(account));
			Account created = resp.readEntity(Account.class);

			//Getting list of accounts
			List<Account> accountsNewList = getAccounts();
			
			//check the list size to be increased by one
			assertEquals(accounts.size() +1, accountsNewList.size() );
	
			assertEquals(account.getIban(), created.getIban());
			assertEquals(account.getAmount(), created.getAmount());
			assertEquals(account.getCreationDate(), created.getCreationDate());
		
			//Deleting test account
			deleteAccount((accountsNewList.get(0).getId()));
			

		}
		@Test
		//@RunAsClient
		public void createAccountNullTest() {
					
			//Creating test account
			Account account =null;
			try{
				accountContoller.create(account);			
				fail("If ex not thrown should fail");
			}catch(Exception e){
				
			}
		}
		
		@Test
		@RunAsClient
		public void createAccountNullCreationDateTest() {
					
			//existing currencies
			List<Account> accounts = getAccounts();

			//Creating test account
			Account account = createAccountNullCreationDateEntity();
			Response resp = target(serviceRelativePath).post(json(account));
			Account created = resp.readEntity(Account.class);

			//Getting list of accounts
			List<Account> accountsNewList = getAccounts();
			
			//check the list size to be increased by one
			assertEquals(accounts.size() +1, accountsNewList.size() );
	
			assertEquals(account.getIban(), created.getIban());
			assertEquals(account.getAmount(), created.getAmount());
			assertNotNull(created.getCreationDate());
		
			//Deleting test account
			deleteAccount((accountsNewList.get(0).getId()));
			

		}
		/**
		 * Test if an account can be obtained by its id
		 */
		@Test
		@RunAsClient
		public void getAccountTest() {
					
			//Creating test currency
			Account account = createAccountEntity();
			Response resp = target(serviceRelativePath).post(json(account));
			Account created = resp.readEntity(Account.class);
			
			resp = target(serviceRelativePath+created.getId()).get();
			Account obtained = resp.readEntity(Account.class);
			
			assertEquals(obtained.getId(), created.getId());
			assertEquals(obtained.getIban(), created.getIban());
			assertEquals(obtained.getAmount(), created.getAmount());
			assertEquals(obtained.getCreationDate(), created.getCreationDate());
			
			//Deleting test account
			deleteAccount(created.getId());

		}
		
		/**
		 * 
		 * Test if an account is updated 
		 */
		@Test
		@RunAsClient
		public void updateAccountTest() {
			Date newCreation = new Date();
			String newIban="IBAN1278DE";
			Double newAmount=3.0d;
			
			//Creating test account
			Account account = createAccount();
			account.setAmount(newAmount);
			account.setIban(newIban);
			account.setCreationDate(newCreation);
			
			Response resp = target(serviceRelativePath).put(json(account));
			Account updated = resp.readEntity(Account.class);

			assertEquals(account.getId(), updated.getId());
			assertEquals(newIban, updated.getIban());
			assertEquals(newAmount, updated.getAmount());
			assertEquals(newCreation, updated.getCreationDate());
			
			//Deleting test currency
			deleteAccount(account.getId());

		}
		
		@Test
		//@RunAsClient
		public void getAccountForCustomerTest(){
			Date newCreation = new Date();
			String newIban="IBAN1278DE";
			Double newAmount=3.0d;
			
			Role role = new Role();
			role.setCreationDate(newCreation);
			role.setName("OneRole");
			role = roleController.create(role);
			
			//Creating test customer
			Customer customer=new Customer();
			customer.setCreationDate(newCreation);
			customer.setDeleted(0);
			customer.setFirstName("CustomerFirst");
			customer.setLastName("CustomerLast");
			customer.setLogin("login");
			customer.setPassword("login");
			customer.setRole(role);
			
			customer = customerController.create(customer);
			assertNotNull(customer.getId());
			
			Currency currency = new Currency();
			currency.setCreationDate(newCreation);
			currency.setSymbol("USD");
			currency.setExchangerate(1.4);
			currency = currencyController.create(currency);
			assertNotNull(currency.getId());
			
			Account account = createAccountEntity();
			account.setIban(newIban);
			account.setAmount(newAmount);
			account.setCreationDate(newCreation);
			account.setCustomer(customer);
			account.setCurrency(currency);
			
			Response resp = target(serviceRelativePath).put(json(account));
			Account accounts = resp.readEntity(Account.class);
			
			
			//assertEquals(account.getId(), accounts.getId());
			assertEquals(newIban,accounts.getIban());
			assertEquals(newAmount, accounts.getAmount());
			assertEquals(newCreation, accounts.getCreationDate());
			
			deleteAccount(accounts.getId());
			customerController.delete(customer.getId());
			currencyController.delete(currency.getId());
			roleController.delete(role.getId());
						
		} 
		
		/**
		 *	Test if an account is deleted 
		 * 
		 */
		@Test
		@RunAsClient
		public void deleteAccountTest() {
			// Creating test account
			createAccount();
			
			// existing accounts
			List<Account> accounts = getAccounts();
			
			//delete test account
			target(serviceRelativePath + accounts.get(0).getId()).delete();
			
			// new accounts list
			List<Account> accountsNewList = getAccounts();
			
			//check the list size to be decrease by one
			assertEquals(accounts.size() - 1, accountsNewList.size() );

		}
		
		/**
		 *	Test if an account is deleted 
		 * 
		 */
		@Test
		@RunAsClient
		public void deleteAccountNoIdTest() {
			
			// existing accounts
			List<Account> accounts = getAccounts();
			
			//delete test account
			target(serviceRelativePath + "-1").delete();
			
			// new accounts list
			List<Account> accountsNewList = getAccounts();
			
			//check the list size to be decrease by one
			assertEquals(accounts.size(), accountsNewList.size() );

		}
		
		/**
		 * 
		 * @return the created entity pojo
		 */
		private Account createAccountEntity(){
			Account account = new Account();
			account.setAmount(amount);
			account.setIban(iban);
			account.setCreationDate(creationDate);
			return account;
		}
		
		private Account createAccountNullCreationDateEntity(){
			Account account = new Account();
			account.setAmount(amount);
			account.setIban(iban);
			account.setCreationDate(null);
			return account;
		}
		
		
		/**
		 * 
		 * @return the created entity in the database
		 */
		private Account createAccount(){
			Account account = createAccountEntity();
			Response resp = target(serviceRelativePath).post(json(account));
			Account created = resp.readEntity(Account.class);
			return created;
		}
		
		/**
		 * 
		 * @return list of existing accounts in the database
		 */
		private List<Account> getAccounts(){
			return target(serviceRelativePath).accept(jsonFormat).get(genericListType);
		}
		
		/**
		 * Deletes the specified entity from db
		 * 
		 * @param id
		 */
		private void deleteAccount(Long id){
			target(serviceRelativePath + id).delete();
		}
		

}