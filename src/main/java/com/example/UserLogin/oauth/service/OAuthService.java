package com.example.UserLogin.oauth.service;

import com.example.UserLogin.oauth.builder.AuthorizationUrlBuilder;
import com.example.UserLogin.oauth.pojo.AccessToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpEntity;

public interface OAuthService {
    String getRedirectUri();
    String getApiKey();
    String getApiSecret();
    String getScope();
    AuthorizationUrlBuilder createAuthorizationUrlBuilder();

    HttpEntity getAccessToken3Legged(String code);

    AccessToken convertJsonTokenToPojo(String accessToken) throws JsonProcessingException;
}
