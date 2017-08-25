package com.bergcomputers.rest.customer;

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

import com.bergcomputers.domain.Currency;
import com.bergcomputers.domain.Customer;
import com.bergcomputers.ejb.AbstractController;
import com.bergcomputers.ejb.ICustomerController;
import com.bergcomputers.rest.AbstractResource;
import com.bergcomputers.rest.util.HeaderUtil;
import com.bergcomputers.rest.util.Page;
import com.bergcomputers.rest.util.PaginationUtil;


@Stateless
@Path("customer")
public class CustomerResource extends AbstractResource {
	private final static Logger log = Logger.getLogger(CustomerResource.class.getName());
	   
	@Context
    private UriInfo uriInfo;
	@EJB
	private ICustomerController customerController;
	
    @GET
    @Produces("application/json")
    @Path("/{customerid}")
    public Customer findCustomer(@PathParam("customerid") Long customerid){
    	return customerController.find(customerid);
    }
    
    /**
     * GET : get all the currencies.
     *
     * @param page the pagination information
     * @param size the pagination size information
     *
     * @return the Response with status 200 (OK) and the list of currencies in
     * body
     * @throws URISyntaxException if there is an error to generate the
     * pagination HTTP headers
     */
    @GET
    @Produces("application/json")
    @Path("/")
    
    public Response findCustomersRange(@QueryParam(PAGE_PARAM) Integer page, @QueryParam(SIZE_PARAM) Integer size) throws URISyntaxException {
        log.debug("REST request to get all Customers");
        List<Customer> customers = new ArrayList<Customer>();
        if (null != page && page > 0 && null != size && size > 0){
        	customers = customerController.findRange((page -1) * size, size);
        }else{
        	customers = customerController.findAll();
        }
        ResponseBuilder builder = Response.ok(customers);
        PaginationUtil.generatePaginationHttpHeaders(builder, new Page(null == page? 1:page, size == null ? -1:size, customerController.count()), "/resources/customer");
        return builder.build();
    }
    
    @DELETE
    @Path("/{customerid}")
    @Produces("application/json")
    public Response deleteCustomer(@PathParam("customerid") Long currencyid){
    	customerController.delete(currencyid);
    	return Response.status(Response.Status.OK).build();
    }
    
    /**
     * POST : Create a new customer.
     *
     * @param customer the customer to create
     * @return the Response with status 201 (Created) and with body the new
     * customer, or with status 400 (Bad Request) if the customer has already
     * an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    
    @POST
    @Produces("application/json")
    @Path("/")
    public Response createCustomer(Customer customer) throws URISyntaxException {
    	log.debug("REST request to save Customer : " + customer);
    	customer = customerController.create(customer);
        
        return HeaderUtil.createEntityCreationAlert
        		(Response.created(new URI("/resources/customer/" + customer.getId())),
                "currency", customer.getId().toString())
                .entity(customer).build();
    }
    
    /**
     * PUT : Updates an existing customer.
     *
     * @param customer the customer to update
     * @return the Response with status 200 (OK) and with body the updated
     * customer, or with status 400 (Bad Request) if the customer is not
     * valid, or with status 500 (Internal Server Error) if the customer
     * couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PUT
    @Produces("application/json")
    @Path("/")
    public Response updateCustomer(Customer customer) throws URISyntaxException {
        log.debug("REST request to update Currency : " + customer);
        customerController.update(customer);
        return HeaderUtil.createEntityUpdateAlert(Response.ok(), "currency", customer.getId().toString())
                .entity(customer).build();
    }
}
