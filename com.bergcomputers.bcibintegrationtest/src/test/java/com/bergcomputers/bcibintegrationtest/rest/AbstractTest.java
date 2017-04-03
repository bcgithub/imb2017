package com.bergcomputers.bcibintegrationtest.rest;

import java.net.URL;
import java.util.Map;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public abstract class AbstractTest {

	 @ArquillianResource
	    private URL deploymentUrl;
	    private WebTarget webTarget;
	   // protected final static MavenResolverSystem RESOLVER = Maven.resolver();

	    @Before
	    public void buildWebTarget() throws Exception {
	        webTarget = ClientBuilder.newClient().target(deploymentUrl.toURI().toString() + "resources/");
	    }

	    protected Invocation.Builder target(String path) {
	        return webTarget.path(path).request();
	    }

	    protected Invocation.Builder target(String path, Map<String, Object> params) {
	        WebTarget target = webTarget.path(path);
	        for (String key : params.keySet()) {
	            if (path.contains(String.format("{%s}", key))) {
	                target = target.resolveTemplate(key, params.get(key));
	            } else {
	                target = target.queryParam(key, params.get(key));
	            }
	        }
	        return target.request();
	    }
}
