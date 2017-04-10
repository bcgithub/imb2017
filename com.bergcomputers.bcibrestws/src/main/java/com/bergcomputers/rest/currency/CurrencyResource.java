package com.bergcomputers.rest.currency;

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

import com.bergcomputers.domain.Currency;
import com.bergcomputers.ejb.AbstractController;
import com.bergcomputers.ejb.ICurrencyController;
import com.bergcomputers.rest.AbstractResource;
import com.bergcomputers.rest.util.HeaderUtil;
import com.bergcomputers.rest.util.Page;
import com.bergcomputers.rest.util.PaginationUtil;

@Stateless
@Path("currency")
public class CurrencyResource extends AbstractResource{
	private final static Logger log = Logger.getLogger(CurrencyResource.class.getName());
   
	@Context
    private UriInfo uriInfo;
	@EJB
	private ICurrencyController currencyController;
	
    @GET
    @Produces("application/json")
    @Path("/{currencyid}")
    public Currency findCurrency(@PathParam("currencyid") Long currencyid){
    	return currencyController.find(currencyid);
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
    public Response findCurrenciesRange(@QueryParam(PAGE_PARAM) Integer page, @QueryParam(SIZE_PARAM) Integer size) throws URISyntaxException {
        log.debug("REST request to get all Currencies");
        List<Currency> currencies = new ArrayList<Currency>();
        if (null != page && page > 0 && null != size && size > 0){
        	currencies = currencyController.findRange((page -1) * size, size);
        }else{
        	currencies = currencyController.findAll();
        }
        ResponseBuilder builder = Response.ok(currencies);
        PaginationUtil.generatePaginationHttpHeaders(builder, new Page(null == page? 1:page, size == null ? -1:size, currencyController.count()), "/resources/currency");
        return builder.build();
    }
    
    @DELETE
    @Path("/{currencyid}")
    @Produces("application/json")
    public Response deleteAccounts(@PathParam("currencyid") Long currencyid){
    	currencyController.delete(currencyid);
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
    public Response createCurrency(Currency currency) throws URISyntaxException {
    	log.debug("REST request to save Currency : " + currency);
    	currency = currencyController.create(currency);
        
        return HeaderUtil.createEntityCreationAlert
        		(Response.created(new URI("/resources/currency/" + currency.getId())),
                "currency", currency.getId().toString())
                .entity(currency).build();
    }
    
    /**
     * PUT : Updates an existing currency.
     *
     * @param currency the currency to update
     * @return the Response with status 200 (OK) and with body the updated
     * currency, or with status 400 (Bad Request) if the currency is not
     * valid, or with status 500 (Internal Server Error) if the currency
     * couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PUT
    @Produces("application/json")
    @Path("/")
    public Response updateCurrency(Currency currency) throws URISyntaxException {
        log.debug("REST request to update Currency : " + currency);
        currencyController.update(currency);
        return HeaderUtil.createEntityUpdateAlert(Response.ok(), "currency", currency.getId().toString())
                .entity(currency).build();
    }
    
    
}
