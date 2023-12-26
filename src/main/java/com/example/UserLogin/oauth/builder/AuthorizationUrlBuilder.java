package com.example.UserLogin.oauth.builder;

import org.springframework.beans.factory.annotation.Value;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.example.UserLogin.oauth.util.Constants.AUTHORIZE_URL;

public class AuthorizationUrlBuilder {

    private final String apiKey;
    private final String redirectUri;
    private final String scope;
    private final String apiSecret;
    private String state;
    private Map<String, String> additionalParams;
//    private final LinkedInOAuthService oauth20Service;

    public AuthorizationUrlBuilder(@Value("${clientId}") String apiKey,
                                   @Value("${redirectUri}") String redirectUri,
                                   @Value("${scope}") String scope,
                                   @Value("${clientSecret}") String apiSecret) {
        this.apiKey = apiKey;
        this.redirectUri = redirectUri;
        this.scope = String.join(",", scope);
        this.apiSecret = apiSecret;
    }

//    public AuthorizationUrlBuilder(final LinkedInOAuthService oauth20Service) {
//        this.oauth20Service = oauth20Service;
//    }

    public AuthorizationUrlBuilder state(final String state) {
        this.state = state;
        return this;
    }


    public String build() throws UnsupportedEncodingException {

        String authoriztaionUrl = AUTHORIZE_URL
                + "?response_type=code&client_id="
                + apiKey
                + "&redirect_uri="
                + redirectUri
                + "&state="
                + state
                + "&scope="
                + URLEncoder.encode(scope, String.valueOf(StandardCharsets.UTF_8));
        return authoriztaionUrl;
    }
}