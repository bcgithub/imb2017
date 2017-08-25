/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bergcomputers.bcibintegrationtest.rest;

import static javax.ws.rs.client.Entity.json;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;

import com.bergcomputers.domain.Role;
import com.bergcomputers.ejb.RoleController;
import com.bergcomputers.ejb.IRoleController;
import com.bergcomputers.rest.role.RoleResource;
/**
 *
 * @author Ionut
 */
public class RoleWSTest extends AbstractTest {

	private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
	
	private String serviceRelativePath = "role/";
	private String jsonFormat = "application/json";
	
	private GenericType<List<Role>> genericListType = new GenericType<List<Role>>() {};

	private String name = "RoleName";
	private Date creationDate = new Date();
	
	 @Inject
	 private IRoleController roleContoller;
	 
	@Deployment
	public static WebArchive createDeployment() {
		return buildArchive().addPackage(RoleResource.class.getPackage()).addPackage(Role.class.getPackage())
				.addPackage(RoleController.class.getPackage()).addClass(RoleResource.class);
	}

	/**
	 * Test if we obtain the list of all currencies
	 */
	@Test
	@RunAsClient
	public void getRolesTest() {
		Role created = createRole();
		List<Role> roles = target(serviceRelativePath).accept(jsonFormat).get(genericListType);
		assertEquals(1, roles.size());
		
		//Deleting test currency
		deleteRole(created.getId());

	}
	
	/**
	 * Test if we obtain the paginated list of all currencies
	 */
	@Test
	@RunAsClient
	public void getRolesPaginationTest() {
		Role created1 = createRole();
		Role created2 = createRole();
		Map<String, Object> params = new HashMap<>();
        params.put("page", 1);
        params.put("size", 1);
        
		List<Role> roles = target(serviceRelativePath, params).accept(jsonFormat).get(genericListType);
		try{
			assertEquals(1, roles.size());
			assertEquals(created1.getId(), roles.get(0).getId());
	        params.put("page", 2);
	        roles = target(serviceRelativePath, params).accept(jsonFormat).get(genericListType);
			assertEquals(1, roles.size());
			assertEquals(created2.getId(), roles.get(0).getId());
		}finally{
			
			//Deleting test currency
			deleteRole(created1.getId());
			deleteRole(created2.getId());
		}

	}

	/**
	 * Test if a currency is created
	 */
	@Test
	@RunAsClient
	public void createRoleTest() {
				
		//existing currencies
		List<Role> roles = getRoles();

		//Creating test currency
		Role role = createRoleEntity();
		Response resp = target(serviceRelativePath).post(json(role));
		Role created = resp.readEntity(Role.class);

		//Getting list of currencies
		List<Role> rolesNewList = getRoles();
		
		//check the list size to be increased by one
		assertEquals(roles.size() +1, rolesNewList.size() );
		
		assertEquals(role.getName(), created.getName());

		assertEquals(role.getCreationDate(), created.getCreationDate());
		
		//Deleting test currency
		deleteRole(rolesNewList.get(0).getId());

	}
	
	/**
	 * Test if a currency can be obtained by its id
	 */
	@Test
	@RunAsClient
	public void getRoleTest() {
				
		//Creating test currency
		Role role = createRoleEntity();
		Response resp = target(serviceRelativePath).post(json(role));
		Role created = resp.readEntity(Role.class);
		
		resp = target(serviceRelativePath+created.getId()).get();
		Role obtained = resp.readEntity(Role.class);
		
		assertEquals(obtained.getId(), created.getId());
		assertEquals(obtained.getName(), created.getName());
		assertEquals(obtained.getCreationDate(), created.getCreationDate());
		
		//Deleting test currency
		deleteRole(created.getId());

	}
	
	/**
	 * 
	 * Test if a currency is updated 
	 */
	@Test
	@RunAsClient
	public void updateRoleTest() {

		String newName = "NewRoleName";
		Date newCreation = new Date();
		
		//Creating test currency
		Role role = createRole();


		role.setName(newName);
		role.setCreationDate(newCreation);
		
		Response resp = target(serviceRelativePath).put(json(role));
		Role updated = resp.readEntity(Role.class);
		
		assertEquals(role.getId(), updated.getId());
		assertEquals(newName, updated.getName());
		assertEquals(newCreation, updated.getCreationDate());
		
		//Deleting test currency
		deleteRole(role.getId());

	}
	
	/**
	 *	Test if a currency is deleted 
	 * 
	 */
	@Test
	@RunAsClient
	public void deleteRoleTest() {
		// Creating test currency
		createRole();
		
		// existing currencies
		List<Role> roles = getRoles();
		
		
		//delete test currency
		target(serviceRelativePath + roles.get(0).getId()).delete();
		
		// new currencies list
		List<Role> rolesNewList = getRoles();
		
		//check the list size to be decrease by one
		assertEquals(roles.size() - 1, rolesNewList.size() );

	}
	
	/**
	 * 
	 * @return the created entity pojo
	 */
	private Role createRoleEntity(){
		Role role = new Role();

		role.setName(name);
		role.setCreationDate(creationDate);
		return role;
	}
	
	/**
	 * 
	 * @return the created entity in the database
	 */
	private Role createRole(){
		Role role = createRoleEntity();
		Response resp = target(serviceRelativePath).post(json(role));
		Role created = resp.readEntity(Role.class);
		return created;
	}
	
	/**
	 * 
	 * @return list of existing currencies in the database
	 */
	private List<Role> getRoles(){
		return target(serviceRelativePath).accept(jsonFormat).get(genericListType);
	}
	
	/**
	 * Deletes the specified entity from db
	 * 
	 * @param id
	 */
	private void deleteRole(Long id){
		target(serviceRelativePath + id).delete();
	}
	
	@Test
	@RunAsClient
	public void deleteRoleTestNoId() {
		// Creating test currency
	//	createRole();
		
		// existing currencies
		List<Role> roles = getRoles();
		
		
		//delete test currency
		target(serviceRelativePath + "-1").delete();
		
		// new currencies list
		List<Role> rolesNewList = getRoles();
		
		//check the list size to be decrease by one
		assertEquals(roles.size(), rolesNewList.size() );
	}
	
	@Test
	@RunAsClient
	public void createRoleTestNoCreationDate() {
				
		//existing currencies
		List<Role> roles = getRoles();

		//Creating test currency
		Role role = createRoleEntity();
		role.setCreationDate(null);
		Response resp = target(serviceRelativePath).post(json(role));
		Role created = resp.readEntity(Role.class);

		//Getting list of currencies
		List<Role> rolesNewList = getRoles();
		
		//check the list size to be increased by one
		assertEquals(roles.size() +1, rolesNewList.size() );
		
		assertEquals(role.getName(), created.getName());

		assertNotNull(created.getCreationDate());
		
		//Deleting test currency
		deleteRole(rolesNewList.get(0).getId());

	}
	@Test
	public void createRoleNullTest() {
		Role role = null;
		
		try{
			roleContoller.create(role);
			fail("If ex not thrown should fail");
		}catch(Exception e){
			
		}
	}
	
	@Test
//	@RunAsClient
	public void getRolesPaginationTestPageMin() {
		Role created1 = createRole();
		Role created2 = createRole();
		Map<String, Object> params = new HashMap<>();
        params.put("page", -3);
        params.put("size", 1);
        
		List<Role> roles = target(serviceRelativePath, params).accept(jsonFormat).get(genericListType);
		try{
			assertEquals(2, roles.size());
			assertEquals(created1.getId(), roles.get(0).getId());
		}finally{
			//Deleting test currency
			deleteRole(created1.getId());
			deleteRole(created2.getId());
		}
	}
	
	@Test
//	@RunAsClient
	public void getRolesPaginationTestPageNull() {
		Role created1 = createRole();
		Role created2 = createRole();
		Map<String, Object> params = new HashMap<>();
        params.put("page", null);
        params.put("size", 1);
        
		List<Role> roles = target(serviceRelativePath, params).accept(jsonFormat).get(genericListType);
		try{
			assertEquals(2, roles.size());
			assertEquals(created1.getId(), roles.get(0).getId());
		}finally{
			//Deleting test currency
			deleteRole(created1.getId());
			deleteRole(created2.getId());
		}
	}
	
	@Test

	public void getRolesPaginationTestSizeNull() {
		Role created1 = createRole();
		Role created2 = createRole();
		Map<String, Object> params = new HashMap<>();
        params.put("page", 1);
        
		List<Role> roles = target(serviceRelativePath, params).accept(jsonFormat).get(genericListType);
		try{
			assertEquals(2, roles.size());
			assertEquals(created1.getId(), roles.get(0).getId());
		}finally{
			//Deleting test currency
			deleteRole(created1.getId());
			deleteRole(created2.getId());
		}


	}
	
	@Test
//	@RunAsClient
	public void getRolesPaginationTestSizeMin() {
		Role created1 = createRole();
		Role created2 = createRole();
		Map<String, Object> params = new HashMap<>();
        params.put("page", 1);
        params.put("size", -3);
        
		List<Role> roles = target(serviceRelativePath, params).accept(jsonFormat).get(genericListType);
		try{
			assertEquals(2, roles.size());
			assertEquals(created1.getId(), roles.get(0).getId());
		}finally{
			//Deleting test currency
			deleteRole(created1.getId());
			deleteRole(created2.getId());
		}
	}
	
}