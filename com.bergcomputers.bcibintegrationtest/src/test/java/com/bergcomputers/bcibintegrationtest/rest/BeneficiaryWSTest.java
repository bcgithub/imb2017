/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bergcomputers.bcibintegrationtest.rest;

import static javax.ws.rs.client.Entity.json;
import static org.junit.Assert.*;

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

import com.bergcomputers.domain.Beneficiary;
import com.bergcomputers.domain.Currency;
import com.bergcomputers.ejb.BeneficiaryController;
import com.bergcomputers.ejb.CurrencyController;
import com.bergcomputers.ejb.IBeneficiaryController;
import com.bergcomputers.ejb.ICurrencyController;
import com.bergcomputers.rest.beneficiary.BeneficiaryResource;
import com.bergcomputers.rest.currency.CurrencyResource;

/**
 *
 * @author Ionut
 */
public class BeneficiaryWSTest extends AbstractTest {

	private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");

	private String serviceRelativePath = "beneficiary/";
	private String jsonFormat = "application/json";

	private GenericType<List<Beneficiary>> genericListType = new GenericType<List<Beneficiary>>() {
	};

	private Date creationDate = new Date();
	private String defaultName = "First";
	private String accountHolderDefault = "Company SA";
	private String details = "Beneficiary details";
	private String defaultIban = "IBAN567890";

	@Inject
	private IBeneficiaryController beneficiaryController;

	@Deployment
	public static WebArchive createDeployment() {
		return buildArchive().addPackage(BeneficiaryResource.class.getPackage())
				.addPackage(Beneficiary.class.getPackage()).addPackage(BeneficiaryController.class.getPackage())
				.addClass(BeneficiaryResource.class).addClass(Beneficiary.class);
	}

	/**
	 * Test if we obtain the list of all beneficiaries
	 */
	@Test
	@RunAsClient
	public void getBeneficiariesTest() {
		Beneficiary created = createBeneficiary();
		List<Beneficiary> beneficiaries = target(serviceRelativePath).accept(jsonFormat).get(genericListType);
		assertEquals(1, beneficiaries.size());

		// Deleting test currency
		deleteBeneficiary(created.getId());

	}

	/**
	 * Test if we obtain the paginated list of all currencies
	 */
	@Test
	@RunAsClient
	public void getBeneficiariesPaginationTest() {
		Beneficiary created1 = createBeneficiary();
		Beneficiary created2 = createBeneficiary();
		Map<String, Object> params = new HashMap<>();
		params.put("page", 1);
		params.put("size", 1);

		List<Beneficiary> beneficiaries = target(serviceRelativePath, params).accept(jsonFormat).get(genericListType);
		assertEquals(1, beneficiaries.size());
		assertEquals(created1.getId(), beneficiaries.get(0).getId());
		params.put("page", 2);
		beneficiaries = target(serviceRelativePath, params).accept(jsonFormat).get(genericListType);
		assertEquals(1, beneficiaries.size());
		assertEquals(created2.getId(), beneficiaries.get(0).getId());

		// Deleting test beneficiary
		deleteBeneficiary(created1.getId());
		deleteBeneficiary(created2.getId());

	}

	/**
	 * Test if a beneficiary is created
	 */
	@Test
	@RunAsClient
	public void createBeneficiaryTest() {

		// existing beneficiaries
		List<Beneficiary> beneficiaries = getBeneficiaries();

		// Creating test beneficiary
		Beneficiary beneficiary = createBeneficiaryEntity();
		Response resp = target(serviceRelativePath).post(json(beneficiary));
		Beneficiary created = resp.readEntity(Beneficiary.class);

		// Getting list of beneficiaries
		List<Beneficiary> beneficiariesNewList = getBeneficiaries();

		// check the list size to be increased by one
		assertEquals(beneficiaries.size() + 1, beneficiariesNewList.size());

		assertEquals(beneficiary.getAccountHolder(), created.getAccountHolder());
		assertEquals(beneficiary.getDetails(), created.getDetails());
		assertEquals(beneficiary.getCreationDate(), created.getCreationDate());

		// Deleting test beneficiary
		deleteBeneficiary(beneficiariesNewList.get(0).getId());

	}

	@Test
	public void createBeneficiaryTestNoCreationDate() {

		// existing beneficiaries
		List<Beneficiary> beneficiaries = getBeneficiaries();

		// Creating test beneficiary
		Beneficiary beneficiary = createBeneficiaryEntityNoCreationDate();
		Response resp = target(serviceRelativePath).post(json(beneficiary));
		Beneficiary created = resp.readEntity(Beneficiary.class);

		// Getting list of beneficiaries
		List<Beneficiary> beneficiariesNewList = getBeneficiaries();

		// check the list size to be increased by one
		assertEquals(beneficiaries.size() + 1, beneficiariesNewList.size());
		assertNotNull(created.getCreationDate());
		assertEquals(beneficiary.getAccountHolder(), created.getAccountHolder());
		assertEquals(beneficiary.getDetails(), created.getDetails());
		//assertEquals(beneficiary.getCreationDate(), created.getCreationDate());

		// Deleting test beneficiary
		deleteBeneficiary(beneficiariesNewList.get(0).getId());

	}

	/**
	 * Test if a beneficiary can be obtained by its id
	 */
	@Test
	@RunAsClient
	public void getBeneficiaryTest() {

		// Creating test beneficiary
		Beneficiary beneficiary = createBeneficiaryEntity();
		Response resp = target(serviceRelativePath).post(json(beneficiary));
		Beneficiary created = resp.readEntity(Beneficiary.class);

		resp = target(serviceRelativePath + created.getId()).get();
		Beneficiary obtained = resp.readEntity(Beneficiary.class);

		assertEquals(obtained.getId(), created.getId());
		assertEquals(obtained.getAccountHolder(), created.getAccountHolder());
		assertEquals(obtained.getDetails(), created.getDetails());
		assertEquals(obtained.getCreationDate(), created.getCreationDate());

		// Deleting test beneficiary
		deleteBeneficiary(created.getId());

	}

	/**
	 * 
	 * Test if a beneficiary is updated
	 */
	@Test
	@RunAsClient
	public void updateBeneficiaryTest() {
		// Double newRate = 3.0d;
		String newSymbol = "EUR";
		Date newCreation = new Date();

		// Creating test beneficiary
		Beneficiary beneficiary = createBeneficiary();

		beneficiary.setDetails("Plata"); // exchange
		// beneficiary.setDetails(newRate);
		beneficiary.setAccountHolder("EUR"); // symbol
		beneficiary.setCreationDate(newCreation);

		Response resp = target(serviceRelativePath).put(json(beneficiary));
		Beneficiary updated = resp.readEntity(Beneficiary.class);

		assertEquals(beneficiary.getId(), updated.getId());
		assertEquals(newSymbol, updated.getAccountHolder());
		// assertEquals(newRate, updated.getDetails());
		assertEquals(newCreation, updated.getCreationDate());

		// Deleting test beneficiary
		deleteBeneficiary(beneficiary.getId());

	}

	/**
	 * Test if a beneficiary is deleted
	 * 
	 */
	@Test
	@RunAsClient
	public void deleteBeneficiaryTest() {
		// Creating test beneficiary
		createBeneficiary(); // fara

		// existing beneficiaries
		List<Beneficiary> beneficiaries = getBeneficiaries();

		// delete test beneficiary
		target(serviceRelativePath + beneficiaries.get(0).getId()).delete();
		
		//IndexOutOfBounds
		// target(serviceRelativePath + beneficiaries.get(-1).getId()).delete();

		// new beneficiaries list
		List<Beneficiary> beneficiariesNewList = getBeneficiaries();

		// check the list size to be decrease by one
		assertEquals(beneficiaries.size() - 1, beneficiariesNewList.size());

	}

	@Test
	public void deleteBeneficiaryTestNull() {
		// Creating test beneficiary
		//createBeneficiary();
		// existing beneficiaries
		List<Beneficiary> beneficiaries = getBeneficiaries();

		// delete test beneficiary
		//IndexOutOfBounds
		//target(serviceRelativePath + beneficiaries.get(-1).getId()).delete();


		// new beneficiaries list
		List<Beneficiary> beneficiariesNewList = getBeneficiaries();

		// check the list size to be decrease by one
		assertEquals(beneficiaries.size(), beneficiariesNewList.size());

	}
	 

	/**
	 * 
	 * @return the created entity pojo
	 */
	private Beneficiary createBeneficiaryEntity() {
		Beneficiary beneficiary = new Beneficiary();
		beneficiary.setDetails(details);
		beneficiary.setAccountHolder(accountHolderDefault);
		beneficiary.setCreationDate(creationDate); // fara
		beneficiary.setName(defaultName);
		beneficiary.setIban(defaultIban);
		return beneficiary;
	}

	private Beneficiary createBeneficiaryEntityNoCreationDate() {
		Beneficiary beneficiary = new Beneficiary();
		beneficiary.setDetails(details);
		beneficiary.setAccountHolder(accountHolderDefault);
		//beneficiary.setCreationDate(); //fara
		beneficiary.setName(defaultName);
		beneficiary.setIban(defaultIban);
		return beneficiary;
	}

	/**
	 * 
	 * @return the created entity in the database
	 */
	private Beneficiary createBeneficiary() {
		Beneficiary beneficiary = createBeneficiaryEntity();
		Response resp = target(serviceRelativePath).post(json(beneficiary));
		Beneficiary created = resp.readEntity(Beneficiary.class);
		return created;
	}

	/**
	 * 
	 * @return list of existing beneficiaries in the database
	 */
	private List<Beneficiary> getBeneficiaries() {
		return target(serviceRelativePath).accept(jsonFormat).get(genericListType);
	}

	/**
	 * Deletes the specified entity from db
	 * 
	 * @param id
	 */
	private void deleteBeneficiary(Long id) {
		target(serviceRelativePath + id).delete();
	}
	
}
