package com.example.UserLogin.oauth.builder;

import com.example.UserLogin.oauth.service.LinkedInOAuthService;
import com.example.UserLogin.oauth.service.OAuthService;
import lombok.*;
import org.springframework.boot.context.properties.ConstructorBinding;


@Getter
@Setter
@Builder
@ConstructorBinding
public class LinkedInOAuthServiceBuilder {

    private  String redirectUri;
    private String apiKey;
    private   String apiSecret;
    private  String scope;
    private  String callback;

    public OAuthService build() {
        return new LinkedInOAuthService(redirectUri, apiKey, apiSecret, scope);
    }


}
