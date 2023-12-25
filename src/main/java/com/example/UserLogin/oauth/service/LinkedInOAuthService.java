package com.example.UserLogin.oauth.service;

import com.example.UserLogin.oauth.builder.AuthorizationUrlBuilder;
import com.example.UserLogin.oauth.pojo.AccessToken;
import com.example.UserLogin.oauth.util.Preconditions;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static com.example.UserLogin.oauth.Constants.USER_AGENT_OAUTH_VALUE;
import static com.example.UserLogin.oauth.util.Constants.*;

/**
 * LinkedIn 3-Legged OAuth Service
 */
@SuppressWarnings({"AvoidStarImport"})
public final class LinkedInOAuthService {

    private final String redirectUri;
    private final String apiKey;
    private final String apiSecret;
    private final String scope;


    private LinkedInOAuthService(final LinkedInOAuthServiceBuilder oauthServiceBuilder) {
        this.redirectUri = oauthServiceBuilder.redirectUri;
        this.apiKey = oauthServiceBuilder.apiKey;
        this.apiSecret = oauthServiceBuilder.apiSecret;
        this.scope = oauthServiceBuilder.scope;

    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public String getScope() {
        return scope;
    }

    public AuthorizationUrlBuilder createAuthorizationUrlBuilder() {
        return new AuthorizationUrlBuilder(this);
    }
    public HttpEntity getAccessToken3Legged(final String code) {

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
        parameters.add(GRANT_TYPE, GrantType.AUTHORIZATION_CODE.getGrantType());
        parameters.add(CODE, code);
        parameters.add(REDIRECT_URI, this.redirectUri);
        parameters.add(CLIENT_ID, this.apiKey);
        parameters.add(CLIENT_SECRET, this.apiSecret);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);
        headers.set(HttpHeaders.USER_AGENT, USER_AGENT_OAUTH_VALUE);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(parameters, headers);
        return request;
    }

    public AccessToken convertJsonTokenToPojo(final String accessToken) throws IOException {
        return new ObjectMapper().readValue(accessToken, AccessToken.class);
    }


    public static final class LinkedInOAuthServiceBuilder {
        private String redirectUri;
        private String apiKey;
        private String apiSecret;
        private String scope;


        public LinkedInOAuthServiceBuilder apiKey(final String apiKey) {
            Preconditions.checkEmptyString(apiKey, "Invalid Api key");
            this.apiKey = apiKey;
            return this;
        }

        public LinkedInOAuthServiceBuilder apiSecret(final String apiSecret) {
            Preconditions.checkEmptyString(apiSecret, "Invalid Api secret");
            this.apiSecret = apiSecret;
            return this;
        }

        public LinkedInOAuthServiceBuilder callback(final String callback) {
            this.redirectUri = callback;
            return this;
        }

        private LinkedInOAuthServiceBuilder setScope(final String scope) {
            Preconditions.checkEmptyString(scope, "Invalid OAuth scope");
            this.scope = scope;
            return this;
        }

        public LinkedInOAuthServiceBuilder defaultScope(final String scope) {
            return setScope(scope);
        }

        public LinkedInOAuthService build() {

            LinkedInOAuthService baseService = new LinkedInOAuthService(this);
            return baseService;
        }
    }





}
