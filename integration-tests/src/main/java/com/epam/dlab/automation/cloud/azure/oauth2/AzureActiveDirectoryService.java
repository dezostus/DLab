package com.epam.dlab.automation.cloud.azure.oauth2;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.oauth.OAuth20Service;

public class AzureActiveDirectoryService extends OAuth20Service {

    private static final String ACCEPTED_FORMAT
            = "application/json; odata=minimalmetadata; streaming=true; charset=utf-8";

    public AzureActiveDirectoryService(DefaultApi20 api, OAuthConfig config) {
        super(api, config);
    }

    @Override
    public void signRequest(String accessToken, OAuthRequest request) {
        super.signRequest(accessToken, request);
        request.addHeader("Accept", ACCEPTED_FORMAT);
    }
}
