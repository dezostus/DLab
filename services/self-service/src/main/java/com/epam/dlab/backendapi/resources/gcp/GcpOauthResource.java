package com.epam.dlab.backendapi.resources.gcp;

import com.epam.dlab.constants.ServiceConsts;
import com.epam.dlab.dto.gcp.auth.GcpOauth2AuthorizationCodeResponse;
import com.epam.dlab.rest.client.RESTService;
import com.epam.dlab.auth.contract.SecurityAPI;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/user/gcp")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GcpOauthResource {

	@Inject
	@Named(ServiceConsts.SECURITY_SERVICE_NAME)
	private RESTService securityService;


	@GET
	@Path("/init")
	public Response redirectedUrl() {
		return Response
				.seeOther(URI.create(securityService.get(SecurityAPI.INIT_LOGIN_OAUTH_GCP, String.class)))
				.build();
	}

	@GET
	@Path("/oauth")
	public Response login(@QueryParam("code") String code, @QueryParam("state") String state,
						  @QueryParam("error") String error) {
		return securityService.post(SecurityAPI.LOGIN_OAUTH,
				new GcpOauth2AuthorizationCodeResponse(code, state, error),
				Response.class);
	}

}
