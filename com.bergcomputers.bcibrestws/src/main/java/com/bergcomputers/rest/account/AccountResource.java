package com.bergcomputers.rest.account;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.jboss.logging.Logger;

import com.bergcomputers.domain.Account;
import com.bergcomputers.domain.Currency;
import com.bergcomputers.domain.Customer;
import com.bergcomputers.ejb.IAccountController;
import com.bergcomputers.ejb.ICurrencyController;
import com.bergcomputers.ejb.ICustomerController;
import com.bergcomputers.rest.AbstractResource;
import com.bergcomputers.rest.currency.CurrencyResource;
import com.bergcomputers.rest.util.HeaderUtil;
import com.bergcomputers.rest.util.Page;
import com.bergcomputers.rest.util.PaginationUtil;

@Stateless
@Path("account")
public class AccountResource extends AbstractResource{

	private final static Logger log = Logger.getLogger(AccountResource.class.getName());
	   
	@Context
    private UriInfo uriInfo;
	@EJB
	private IAccountController accountController;
	@EJB
	private ICustomerController customerController;
	
	
    @GET
    @Produces("application/json")
    @Path("/{accountid}")
    public Account findAccount(@PathParam("accountid") Long accountid){
    	return accountController.find(accountid);
    }
    
    @GET
    @Produces("application/json")
    @Path("/customer/{customerid}")
    public List<Account> findAccountsForCustomers(@PathParam("customerid") Long customerid){
    	return accountController.getAccountForCustomer(customerid);
    }
    
    /**
     * GET : get all the accounts.
     *
     * @param page the pagination information
     * @param size the pagination size information
     *
     * @return the Response with status 200 (OK) and the list of accounts in
     * body
     * @throws URISyntaxException if there is an error to generate the
     * pagination HTTP headers
     */
    @GET
    @Produces("application/json")
    @Path("/")
    public Response findAccountsRange(@QueryParam(PAGE_PARAM) Integer page, @QueryParam(SIZE_PARAM) Integer size) throws URISyntaxException {
        log.debug("REST request to get all Accounts");
        List<Account> accounts = new ArrayList<Account>();
        if (null != page && page > 0 && null != size && size > 0){
        	accounts = accountController.findRange((page -1) * size, size);
        }else{
        	accounts = accountController.findAll();
        }
        ResponseBuilder builder = Response.ok(accounts);
        PaginationUtil.generatePaginationHttpHeaders(builder, new Page(null == page? 1:page, size == null ? -1:size, accountController.count()), "/resources/account");
        return builder.build();
    }
    
    @DELETE
    @Path("/{accountid}")
    @Produces("application/json")
    public Response deleteAccounts(@PathParam("accountid") Long accountid){
    	accountController.delete(accountid);
    	return Response.status(Response.Status.OK).build();
    }
    /**
     * POST : Create a new account.
     *
     * @param account the account to create
     * @return the Response with status 201 (Created) and with body the new
     * account, or with status 400 (Bad Request) if the account has already
     * an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    
    @POST
    @Produces("application/json")
    @Path("/")
    public Response createAccount(Account account) throws URISyntaxException {
    	log.debug("REST request to save Account : " + account);
    	account = accountController.create(account);
        
        return HeaderUtil.createEntityCreationAlert
        		(Response.created(new URI("/resources/account/" + account.getId())),
                "account", account.getId().toString())
                .entity(account).build();
    }
    
    /**
     * PUT : Updates an existing account.
     *
     * @param account the account to update
     * @return the Response with status 200 (OK) and with body the updated
     * account, or with status 400 (Bad Request) if the account is not
     * valid, or with status 500 (Internal Server Error) if the account
     * couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PUT
    @Produces("application/json")
    @Path("/")
    public Response updateAccount(Account account) throws URISyntaxException {
        log.debug("REST request to update Account : " + account);
        Account updaccount = accountController.update(account);
        return HeaderUtil.createEntityUpdateAlert(Response.ok(), "account",updaccount.getId().toString())
                .entity(updaccount).build();
    }
}
