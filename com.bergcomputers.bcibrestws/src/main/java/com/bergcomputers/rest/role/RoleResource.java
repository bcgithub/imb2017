package com.bergcomputers.rest.role;

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

import com.bergcomputers.domain.Role;
import com.bergcomputers.ejb.AbstractController;
import com.bergcomputers.ejb.IRoleController;
import com.bergcomputers.rest.AbstractResource;
import com.bergcomputers.rest.util.HeaderUtil;
import com.bergcomputers.rest.util.Page;
import com.bergcomputers.rest.util.PaginationUtil;

@Stateless
@Path("role")
public class RoleResource extends AbstractResource{
	private final static Logger log = Logger.getLogger(RoleResource.class.getName());
   
	@Context
    private UriInfo uriInfo;
	@EJB
	private IRoleController roleController;
	
    @GET
    @Produces("application/json")
    @Path("/{roleid}")
    public Role findRole(@PathParam("roleid") Long roleid){
    	return roleController.find(roleid);
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
    public Response findRolesRange(@QueryParam(PAGE_PARAM) Integer page, @QueryParam(SIZE_PARAM) Integer size) throws URISyntaxException {
        log.debug("REST request to get all Roles");
        List<Role> roles = new ArrayList<Role>();
        if (null != page && page > 0 && null != size && size > 0){
        	roles = roleController.findRange((page -1) * size, size);
        }else{
        	roles = roleController.findAll();
        }
        ResponseBuilder builder = Response.ok(roles);
        PaginationUtil.generatePaginationHttpHeaders(builder, new Page(null == page? 1:page, size == null ? -1:size, roleController.count()), "/resources/role");
        return builder.build();
    }
    
    @DELETE
    @Path("/{roleid}")
    @Produces("application/json")
    public Response deleteAccounts(@PathParam("roleid") Long roleid){
    	roleController.delete(roleid);
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
    public Response createRole(Role role) throws URISyntaxException {
    	log.debug("REST request to save Role : " + role);
    	role = roleController.create(role);
        
        return HeaderUtil.createEntityCreationAlert
        		(Response.created(new URI("/resources/role/" + role.getId())),
                "role", role.getId().toString())
                .entity(role).build();
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
    public Response updateRole(Role role) throws URISyntaxException {
        log.debug("REST request to update Role : " + role);
        roleController.update(role);
        return HeaderUtil.createEntityUpdateAlert(Response.ok(), "role", role.getId().toString())
                .entity(role).build();
    }
    
    
}