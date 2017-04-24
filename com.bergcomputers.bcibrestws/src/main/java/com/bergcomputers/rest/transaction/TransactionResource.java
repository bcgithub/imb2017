package com.bergcomputers.rest.transaction;

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

import com.bergcomputers.domain.Transaction;
import com.bergcomputers.ejb.ITransactionController;
import com.bergcomputers.rest.AbstractResource;
import com.bergcomputers.rest.util.HeaderUtil;
import com.bergcomputers.rest.util.Page;
import com.bergcomputers.rest.util.PaginationUtil;

@Stateless
@Path("transaction")
public class TransactionResource extends AbstractResource
{
	private final static Logger log = Logger.getLogger(TransactionResource.class.getName());
	
	@Context
    private UriInfo uriInfo;
	@EJB
	private ITransactionController transactionController;
	
    @GET
    @Produces("application/json")
    @Path("/{transactionid}")
    public Transaction findTransaction(@PathParam("transactionid") Long transactionid){
    	return transactionController.find(transactionid);
    }
    
    @GET
    @Produces("application/json")
    @Path("/")
    public Response findTransactionRange(@QueryParam(PAGE_PARAM) Integer page, @QueryParam(SIZE_PARAM) Integer size) throws URISyntaxException {
        log.debug("REST request to get all Transaction");
        List<Transaction> transactions = new ArrayList<Transaction>();
        if (null != page && page > 0 && null != size && size > 0){
        	transactions = transactionController.findRange((page -1) * size, size);
        }else{
        	transactions = transactionController.findAll();
        }
        ResponseBuilder builder = Response.ok(transactions);
        PaginationUtil.generatePaginationHttpHeaders(builder, new Page(null == page? 1:page, size == null ? -1:size, transactionController.count()), "/resources/transaction");
        return builder.build();
    }
    
    @DELETE
    @Path("/{transactionid}")
    @Produces("application/json")
    public Response deleteAccounts(@PathParam("transactionid") Long transactionid){
    	transactionController.delete(transactionid);
    	return Response.status(Response.Status.OK).build();
    }
    
    @POST
    @Produces("application/json")
    @Path("/")
    public Response createTransaction(Transaction transaction) throws URISyntaxException {
    	log.debug("REST request to save Transaction : " + transaction);
    	transaction = transactionController.create(transaction);
        
        return HeaderUtil.createEntityCreationAlert
        		(Response.created(new URI("/resources/transaction/" + transaction.getId())),
                "transaction", transaction.getId().toString())
                .entity(transaction).build();
    }
    
    @PUT
    @Produces("application/json")
    @Path("/")
    public Response updateTransaction(Transaction transaction) throws URISyntaxException {
        log.debug("REST request to update Transaction : " + transaction);
        transactionController.update(transaction);
        return HeaderUtil.createEntityUpdateAlert(Response.ok(), "transaction", transaction.getId().toString())
                .entity(transaction).build();
    }
}
