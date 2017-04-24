package com.bergcomputers.rest.beneficiary;

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
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.jboss.logging.Logger;

import com.bergcomputers.domain.Beneficiary;
import com.bergcomputers.domain.Currency;
import com.bergcomputers.ejb.AbstractController;
import com.bergcomputers.ejb.IBeneficiaryController;
import com.bergcomputers.ejb.ICurrencyController;
import com.bergcomputers.rest.AbstractResource;
import com.bergcomputers.rest.util.HeaderUtil;
import com.bergcomputers.rest.util.Page;
import com.bergcomputers.rest.util.PaginationUtil;

@Stateless
@Path("currency")
public class BeneficiaryResource extends AbstractResource{
	private final static Logger log = Logger.getLogger(BeneficiaryResource.class.getName());
   
	@Context
    private UriInfo uriInfo;
	@EJB
	private IBeneficiaryController beneficiaryController;
	
    @GET
    @Produces("application/json")
    @Path("/{beneficiaryid}")
    public Beneficiary findBeneficiary(@PathParam("beneficiaryid") Long beneficiaryid){
    	return beneficiaryController.find(beneficiaryid);
    }
 
    
    /**
     * GET : get all the beneficiaries.
     *
     * @param page the pagination information
     * @param size the pagination size information
     *
     * @return the Response with status 200 (OK) and the list of beneficiaries in
     * body
     * @throws URISyntaxException if there is an error to generate the
     * pagination HTTP headers
     */
    @GET
    @Produces("application/json")
    @Path("/")
    public Response findBeneficiariesRange(@QueryParam(PAGE_PARAM) Integer page, @QueryParam(SIZE_PARAM) Integer size) throws URISyntaxException {
        log.debug("REST request to get all Beneficiaries");
        List<Beneficiary> beneficiaries = new ArrayList<Beneficiary>();
        if (null != page && page > 0 && null != size && size > 0){
        	beneficiaries = beneficiaryController.findRange((page -1) * size, size);
        }else{
        	beneficiaries = beneficiaryController.findAll();
        }
        ResponseBuilder builder = Response.ok(beneficiaries);
        PaginationUtil.generatePaginationHttpHeaders(builder, new Page(null == page? 1:page, size == null ? -1:size, beneficiaryController.count()), "/resources/currency");
        return builder.build();
    }
    
    @DELETE
    @Path("/{beneficiaryid}")
    @Produces("application/json")
    public Response deleteAccounts(@PathParam("beneficiaryid") Long beneficiaryid){
    	beneficiaryController.delete(beneficiaryid);
    	return Response.status(Response.Status.OK).build();
    }
    
    /**
     * POST : Create a new currency.
     *
     * @param currency the currency to create
     * @return the Response with status 201 (Created) and with body the new
     * currency, or with status 400 (Bad Request) if the currency has already
     * an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    
    @POST
    @Produces("application/json")
    @Path("/")
    public Response createBeneficiary(Beneficiary beneficiary) throws URISyntaxException {
    	log.debug("REST request to save Currency : " + beneficiary);
    	beneficiary = beneficiaryController.create(beneficiary);
        
        return HeaderUtil.createEntityCreationAlert
        		(Response.created(new URI("/resources/beneficiary/" + beneficiary.getId())),
                "beneficiary", beneficiary.getId().toString())
                .entity(beneficiary).build();
    }
    
    /**
     * PUT : Updates an existing beneficiary.
     *
     * @param beneficiary the beneficiary to update
     * @return the Response with status 200 (OK) and with body the updated
     * beneficiary, or with status 400 (Bad Request) if the beneficiary is not
     * valid, or with status 500 (Internal Server Error) if the beneficiary
     * couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PUT
    @Produces("application/json")
    @Path("/")
    public Response updateBeneficiary(Beneficiary beneficiary) throws URISyntaxException {
        log.debug("REST request to update Beneficiary : " + beneficiary);
        beneficiaryController.update(beneficiary);
        return HeaderUtil.createEntityUpdateAlert(Response.ok(), "beneficiary", beneficiary.getId().toString())
                .entity(beneficiary).build();
    }
    
    
}
