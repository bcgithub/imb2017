package com.bergcomputers.rest.device;

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

import com.bergcomputers.domain.Device;
import com.bergcomputers.ejb.AbstractController;
import com.bergcomputers.ejb.IDeviceController;
import com.bergcomputers.rest.AbstractResource;
import com.bergcomputers.rest.util.HeaderUtil;
import com.bergcomputers.rest.util.Page;
import com.bergcomputers.rest.util.PaginationUtil;

@Stateless
@Path("device")
public class DeviceResource extends AbstractResource{
	private final static Logger log = Logger.getLogger(DeviceResource.class.getName());
   
	@Context
    private UriInfo uriInfo;
	@EJB
	private IDeviceController deviceController;
	
    @GET
    @Produces("application/json")
    @Path("/{deviceid}")
    public Device findDevice(@PathParam("deviceid") Long deviceid){
    	return deviceController.find(deviceid);
    }
 
    
    /**
     * GET : get all the devices.
     *
     * @param page the pagination information
     * @param size the pagination size information
     *
     * @return the Response with status 200 (OK) and the list of devices in
     * body
     * @throws URISyntaxException if there is an error to generate the
     * pagination HTTP headers
     */
    @GET
    @Produces("application/json")
    @Path("/")
    
    public Response findDevicesRange(@QueryParam(PAGE_PARAM) Integer page, @QueryParam(SIZE_PARAM) Integer size) throws URISyntaxException {
        log.debug("REST request to get all Devices");
        List<Device> devices = new ArrayList<Device>();
        if (null != page && page > 0 && null != size && size > 0){
        	devices = deviceController.findRange((page -1) * size, size);
        }else{
        	devices = deviceController.findAll();
        }
        ResponseBuilder builder = Response.ok(devices);
        PaginationUtil.generatePaginationHttpHeaders(builder, new Page(null == page? 1:page, size == null ? -1:size, deviceController.count()), "/resources/device");
        return builder.build();
    }
    
    @DELETE
    @Path("/{deviceid}")
    @Produces("application/json")
    public Response deleteAccount(@PathParam("deviceid") Long deviceid){
    	deviceController.delete(deviceid);
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
    public Response createDevice(Device device) throws URISyntaxException {
    	log.debug("REST request to save Device : " + device);
    	device = deviceController.create(device);
        
        return HeaderUtil.createEntityCreationAlert
        		(Response.created(new URI("/resources/device/" + device.getId())),
                "device", device.getId().toString())
                .entity(device).build();
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
    public Response updateDevice(Device device) throws URISyntaxException {
        log.debug("REST request to update Device : " + device);
        deviceController.update(device);
        return HeaderUtil.createEntityUpdateAlert(Response.ok(), "device", device.getId().toString())
                .entity(device).build();
    }
    
    
}