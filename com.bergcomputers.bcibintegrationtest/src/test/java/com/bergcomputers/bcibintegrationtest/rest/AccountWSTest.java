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

import com.bergcomputers.domain.Account;
import com.bergcomputers.ejb.AccountController;
import com.bergcomputers.ejb.IAccountController;
import com.bergcomputers.rest.account.AccountResource;

public class AccountWSTest extends AbstractTest{

private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
	
	private String serviceRelativePath = "account/";
	private String jsonFormat = "application/json";
	
	private GenericType<List<Account>> genericListType = new GenericType<List<Account>>() {};

	private Date creationDate = new Date();
	private String iban="IBAN1234DE";
	private Double amount;
	
	 @Inject
	 private IAccountController accountContoller;
	 
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

			//Getting list of currencies
			List<Account> accountsNewList = getAccounts();
			
			//check the list size to be increased by one
			assertEquals(accounts.size() +1, accountsNewList.size() );
	
			assertEquals(account.getIban(), created.getIban());
			assertEquals(account.getAmount(), created.getAmount());
			assertEquals(account.getCreationDate(), created.getCreationDate());
		
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
			String newIban="";
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