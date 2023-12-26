package com.example.UserLogin.oauth.service;

import com.example.UserLogin.oauth.builder.AuthorizationUrlBuilder;
import com.example.UserLogin.oauth.pojo.AccessToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import static com.example.UserLogin.oauth.Constants.USER_AGENT_OAUTH_VALUE;
import static com.example.UserLogin.oauth.util.Constants.*;
/**
 * LinkedIn 3-Legged OAuth Service
 */

@Service
public class LinkedInOAuthService implements OAuthService {

    private final String redirectUri;
    private final String apiKey;
    private final String apiSecret;
    private final String scope;

    @Autowired
    private  RestTemplate restTemplate;

    @Autowired
    public LinkedInOAuthService(
            @Value("${redirectUri}") String redirectUri,
            @Value("${clientId}") String apiKey,
            @Value("${clientSecret}") String apiSecret,
            @Value("${scope}") String scope
            ) {
        this.redirectUri = redirectUri;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.scope = scope;
    }

    @Override
    public String getRedirectUri() {
        return redirectUri;
    }

    @Override
    public String getApiKey() {
        return apiKey;
    }

    @Override
    public String getApiSecret() {
        return apiSecret;
    }

    @Override
    public String getScope() {
        return scope;
    }

    @Override
    public AuthorizationUrlBuilder createAuthorizationUrlBuilder() {
        return new AuthorizationUrlBuilder(getApiKey(), getRedirectUri(), getScope(),getApiSecret());
    }

    @Override
    public HttpEntity getAccessToken3Legged(String code) {

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

    @Override
    public AccessToken convertJsonTokenToPojo(String accessToken) throws JsonProcessingException {
        return new ObjectMapper().readValue(accessToken, AccessToken.class);
    }

}