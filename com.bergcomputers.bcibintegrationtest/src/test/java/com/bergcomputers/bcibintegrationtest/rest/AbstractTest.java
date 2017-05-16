package com.bergcomputers.bcibintegrationtest.rest;

import java.io.File;
import java.net.URL;
import java.util.Map;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolverSystem;
import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public abstract class AbstractTest {

	//@ArquillianResource
	protected URL deploymentUrl;
	protected WebTarget webTarget;
	protected final static MavenResolverSystem RESOLVER = Maven.resolver();

	public static WebArchive buildArchive() {
	    File[] jacksonFiles = RESOLVER.resolve("com.fasterxml.jackson.jaxrs:jackson-jaxrs-json-provider:2.7.5").withTransitivity().asFile();
	       
		return ShrinkWrap.create(WebArchive.class, "test.war")
				//.addPackage(JaxRsActivator.class.getPackage())
				//.addClass(JaxRsActivator.class)
				.addAsLibraries(jacksonFiles)
				.addAsResource("test-persistence.xml", "META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.setWebXML("web.xml");
	}

	@Before
	public void buildWebTarget() throws Exception {
		if (null == deploymentUrl){
			deploymentUrl = new URL("http://localhost:18080/test/");
		}
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
