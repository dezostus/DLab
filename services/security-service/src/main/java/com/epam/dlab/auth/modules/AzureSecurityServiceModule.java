package com.epam.dlab.auth.modules;

import com.epam.dlab.auth.SecurityServiceConfiguration;
import com.epam.dlab.auth.UserInfoDAO;
import com.epam.dlab.auth.UserVerificationService;
import com.epam.dlab.auth.azure.AzureAuthenticationResource;
import com.epam.dlab.auth.azure.AzureLoginUrlBuilder;
import com.epam.dlab.auth.azure.AzureSecurityResource;
import com.epam.dlab.auth.azure.service.AzureAuthorizationCodeService;
import com.epam.dlab.auth.azure.service.AzureAuthorizationCodeServiceImpl;
import com.epam.dlab.auth.conf.AzureLoginConfiguration;
import com.epam.dlab.auth.resources.SynchronousLdapAuthenticationService;
import com.epam.dlab.cloud.CloudModule;
import com.google.inject.Injector;
import io.dropwizard.setup.Environment;

import java.io.IOException;

public class AzureSecurityServiceModule extends CloudModule {
	private final SecurityServiceConfiguration conf;

	AzureSecurityServiceModule(SecurityServiceConfiguration configuration) {
		this.conf = configuration;
	}

	@Override
	protected void configure() {
		bind(UserVerificationService.class).toInstance(SecurityServiceModule.defaultUserVerificationService());
		final AzureLoginConfiguration azureLoginConfiguration = conf.getAzureLoginConfiguration();
		bind(AzureLoginConfiguration.class).toInstance(azureLoginConfiguration);
		if (!azureLoginConfiguration.isUseLdap()) {
			bind(AzureLoginUrlBuilder.class).toInstance(new AzureLoginUrlBuilder(azureLoginConfiguration));
			try {
				final AzureAuthorizationCodeServiceImpl authorizationCodeService = new
						AzureAuthorizationCodeServiceImpl(azureLoginConfiguration.getAuthority() +
						azureLoginConfiguration.getTenant() + "/", azureLoginConfiguration
						.getPermissionScope(), azureLoginConfiguration.getManagementApiAuthFile(),
						azureLoginConfiguration.isValidatePermissionScope());
				bind(AzureAuthorizationCodeService.class).toInstance(authorizationCodeService);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public void init(Environment environment, Injector injector) {

		if (conf.getAzureLoginConfiguration().isUseLdap()) {
			environment.jersey().register(injector.getInstance(SynchronousLdapAuthenticationService.class));
		} else {
			final AzureAuthenticationResource azureAuthenticationResource = new AzureAuthenticationResource(conf,
					injector.getInstance(UserInfoDAO.class), conf.getAzureLoginConfiguration(),
					injector.getInstance(AzureAuthorizationCodeService.class));
			environment.jersey().register(azureAuthenticationResource);
			environment.jersey().register(injector.getInstance(AzureSecurityResource.class));
		}
	}
}
