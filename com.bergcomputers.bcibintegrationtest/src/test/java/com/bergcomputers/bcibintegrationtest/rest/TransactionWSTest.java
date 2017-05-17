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
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.bergcomputers.domain.Account;
import com.bergcomputers.domain.Currency;
import com.bergcomputers.domain.Customer;
import com.bergcomputers.domain.Role;
import com.bergcomputers.domain.Transaction;
import com.bergcomputers.ejb.IAccountController;
import com.bergcomputers.ejb.ICurrencyController;
import com.bergcomputers.ejb.ICustomerController;
import com.bergcomputers.ejb.IRoleController;
import com.bergcomputers.ejb.ITransactionController;
import com.bergcomputers.ejb.TransactionController;
import com.bergcomputers.rest.transaction.TransactionResource;

@RunWith(Arquillian.class)
public class TransactionWSTest extends AbstractTest
{
	private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
	
	private String serviceRelativePath = "transaction/";
	private String accountRelativePath = "account/";
	private String jsonFormat = "application/json";
	
	private GenericType<List<Transaction>> genericListType = new GenericType<List<Transaction>>() {};

	private Account defaultAccount = new Account();
	private String defaultType = "Default Type";
	private Double defaultAmount = 10.0;
	private String defaultSender = "Default Sender";
	private String defaultDetails = "Default Details";
	private String defaultStatus = "Default Status";
	private Date creationDate = new Date();
	
	private Role defaultRole = new Role();
	private Currency defaultCurrency = new Currency();
	private Customer defaultCustomer = new Customer();
	
	@Inject
	private ITransactionController transactionController;
	 
	@Inject
	private IAccountController accountController;
	
	@Inject
	private ICurrencyController currencyController;
	
	@Inject
	private ICustomerController customerController;
	
	@Inject
	private IRoleController roleController;
	 
	@Deployment
	public static WebArchive createDeployment() {
		return buildArchive().addPackage(TransactionResource.class.getPackage()).addPackage(Transaction.class.getPackage())
				.addPackage(TransactionController.class.getPackage());
	}
	
	/**
	 * Test if we obtain the list of all currencies
	 */
	@Test
	//@RunAsClient
	public void getTransactionsTest() {
		Transaction created = createTransaction();
		List<Transaction> transactions = target(serviceRelativePath).accept(jsonFormat).get(genericListType);
		assertEquals(1, transactions.size());
		
		//Deleting test transaction
		deleteTransaction(created.getId());
	}
	
	/**
	 * Test if we obtain the paginated list of all transactions
	 */
	@Test
	//@RunAsClient
	public void getTransactionsPaginationTest() {
		Transaction created1 = createTransaction();
		Transaction created2 = createTransaction();
		Map<String, Object> params = new HashMap<>();
        params.put("page", 1);
        params.put("size", 1);
        
		List<Transaction> transactions = target(serviceRelativePath, params).accept(jsonFormat).get(genericListType);
		assertEquals(1, transactions.size());
		assertEquals(created1.getId(), transactions.get(0).getId());
        params.put("page", 2);
        transactions = target(serviceRelativePath, params).accept(jsonFormat).get(genericListType);
		assertEquals(1, transactions.size());
		assertEquals(created2.getId(), transactions.get(0).getId());
		
		//Deleting test transaction
		deleteTransaction(created1.getId());
		deleteTransaction(created2.getId());

	}

	/**
	 * Test if a transaction is created
	 */
	//@Test
	@RunAsClient
	public void createTransactionTest() {
				
		//existing transactions
		List<Transaction> transactions = getTransactions();

		//Creating test transaction
		Transaction transaction = createTransactionEntity();
		Response resp = target(serviceRelativePath).post(json(transaction));
		Transaction created = resp.readEntity(Transaction.class);

		//Getting list of transactions
		List<Transaction> transactionsNewList = getTransactions();
		
		//check the list size to be increased by one
		assertEquals(transactions.size() +1, transactionsNewList.size() );
		
		assertEquals(transaction.getDate(), created.getDate());
		assertEquals(transaction.getAccount(), created.getAccount());
		assertEquals(transaction.getTransactionDate(), created.getTransactionDate());
		assertEquals(transaction.getTransactionType(), created.getTransactionType());
		assertEquals(transaction.getAmount(), created.getAmount());
		assertEquals(transaction.getSender(), created.getSender());
		assertEquals(transaction.getDetails(), created.getDetails());
		assertEquals(transaction.getStatus(), created.getStatus());
		
		//Deleting test transaction
		deleteTransaction(transactionsNewList.get(0).getId());

	}	
	
	/**
	 * Test if a transaction can be obtained by its id
	 */
	//@Test
	@RunAsClient
	public void getTransactionTest() {
				
		//Creating test transaction
		Transaction transaction = createTransactionEntity();
		Response resp = target(serviceRelativePath).post(json(transaction));
		Transaction created = resp.readEntity(Transaction.class);
		
		resp = target(serviceRelativePath+created.getId()).get();
		Transaction obtained = resp.readEntity(Transaction.class);
		
		assertEquals(obtained.getId(), created.getId());
		assertEquals(obtained.getDate(), created.getDate());
		assertEquals(obtained.getAccount(), created.getAccount());
		assertEquals(obtained.getTransactionDate(), created.getTransactionDate());
		assertEquals(obtained.getTransactionType(), created.getTransactionType());
		assertEquals(obtained.getAmount(), created.getAmount());
		assertEquals(obtained.getSender(), created.getSender());
		assertEquals(obtained.getDetails(), created.getDetails());
		assertEquals(obtained.getStatus(), created.getStatus());
		assertEquals(obtained.getCreationDate(), created.getCreationDate());
		
		//Deleting test transaction
		deleteTransaction(created.getId());

	}
	
	/**
	 * 
	 * Test if a transaction is updated 
	 */
	//@Test
	@RunAsClient
	public void updateTransactionTest() {
		
		Date newCreation = new Date();
		Account newAccount = new Account();
		String newType = "newType";
		Double newAmount = 10.0;
		String newSender = "newSender";
		String newDetails = "newDetails";
		String newStatus = "newStatus";
		
		//Creating test transaction
		Transaction transaction = createTransaction();

		transaction.setDate(newCreation);
		transaction.setAccount(newAccount);
		transaction.setTransactionDate(newCreation);
		transaction.setTransactionType(newType);
		transaction.setAmount(newAmount);
		transaction.setSender(newSender);
		transaction.setDetails(newDetails);
		transaction.setStatus(newStatus);
		transaction.setCreationDate(newCreation);
		
		Response resp = target(serviceRelativePath).put(json(transaction));
		Transaction updated = resp.readEntity(Transaction.class);
		
		assertEquals(transaction.getId(), updated.getId());
		assertEquals(newAccount, updated.getAccount());
		assertEquals(newCreation, updated.getTransactionDate());
		assertEquals(newType, updated.getTransactionType());
		assertEquals(newAmount, updated.getAmount());
		assertEquals(newSender, updated.getSender());
		assertEquals(newDetails, updated.getDetails());
		assertEquals(newStatus, updated.getStatus());
		assertEquals(newCreation, updated.getCreationDate());
		
		//Deleting test transaction
		deleteTransaction(transaction.getId());

	}
	
	/**
	 *	Test if a transaction is deleted 
	 * 
	 */
	//@Test
	@RunAsClient
	public void deleteTransactionTest() {
		// Creating test transaction
		createTransaction();
		
		// existing transactions
		List<Transaction> transactions = getTransactions();
		
		
		//delete test transaction
		target(serviceRelativePath + transactions.get(0).getId()).delete();
		
		// new transactions list
		List<Transaction> transactionsNewList = getTransactions();
		
		//check the list size to be decrease by one
		assertEquals(transactions.size() - 1, transactionsNewList.size() );

	}
	
	/**
	 * 
	 * @return the created entity pojo
	 */
	private Transaction createTransactionEntity(){
		
		defaultCurrency.setExchangerate(2.0);
		defaultCurrency.setSymbol("USD");
		defaultCurrency = currencyController.create(defaultCurrency);
		
		defaultRole.setName("User");
		defaultRole = roleController.create(defaultRole);
		
		defaultCustomer.setFirstName("Customer1");
		defaultCustomer.setLastName("LastName");
		defaultCustomer.setLogin("c1login");
		defaultCustomer.setPassword("c1pwd");
		defaultCustomer.setRole(defaultRole);
		defaultCustomer = customerController.create(defaultCustomer);
		
		defaultAccount.setAmount(100d);
		defaultAccount.setCreationDate(new Date());
		defaultAccount.setCurrency(defaultCurrency);
		defaultAccount.setCustomer(defaultCustomer);
		defaultAccount.setDeleted(0);
		defaultAccount.setIban("0000000000");
		defaultAccount.setId(99L);
		
		defaultAccount = accountController.create(defaultAccount);
		
		Transaction transaction = new Transaction();
		transaction.setDate(creationDate);
		transaction.setAccount(defaultAccount);
		transaction.setTransactionDate(creationDate);
		transaction.setTransactionType(defaultType);
		transaction.setAmount(defaultAmount);
		transaction.setSender(defaultSender);
		transaction.setDetails(defaultDetails);
		transaction.setStatus(defaultStatus);
		transaction.setCreationDate(creationDate);
		
		//accountController.delete(99);
		return transaction;
	}
	
	/**
	 * 
	 * @return the created entity in the database
	 */
	private Transaction createTransaction(){
		Transaction transaction = createTransactionEntity();
		Response resp = target(serviceRelativePath).post(json(transaction));
		Transaction created = resp.readEntity(Transaction.class);
		return created;
	}
	
	/**
	 * 
	 * @return list of existing transactions in the database
	 */
	private List<Transaction> getTransactions(){
		return target(serviceRelativePath).accept(jsonFormat).get(genericListType);
	}
	

	/**
	 * Deletes the specified entity from db
	 * 
	 * @param id
	 */
	private void deleteTransaction(Long id){
		target(serviceRelativePath + id).delete();
	}
}
