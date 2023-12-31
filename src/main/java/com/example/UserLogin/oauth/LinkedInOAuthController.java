package com.example.UserLogin.oauth;

import com.example.UserLogin.exceptions.UserException;
import com.example.UserLogin.oauth.builder.LinkedInOAuthServiceBuilder;
import com.example.UserLogin.oauth.pojo.AccessToken;
import com.example.UserLogin.oauth.service.LinkedInOAuthService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;


import static com.example.UserLogin.oauth.util.Constants.REQUEST_TOKEN_URL;
import static com.example.UserLogin.oauth.util.Constants.USERINFO_URL;

@RestController
@Slf4j
public class LinkedInOAuthController {


    private final RestTemplate restTemplate;
    private final LinkedInOAuthService service;
    private Properties prop = new Properties();
    private String propFileName = "application.properties";

    @Value("${clientId}")
    private String clientId;

    @Value("${clientSecret}")
    private String clientSecret;

    @Value("${scope}")
    private String scope;

    @Value("${redirectUri}")
    private String redirectUri;

    private String token;

    private String refreshToken;

    @Autowired
    public LinkedInOAuthController(RestTemplate restTemplate, LinkedInOAuthService service) {
        this.restTemplate = restTemplate;
        this.service = service;
    }


    @RequestMapping("/linkedin/login")
    public RedirectView oauth(@RequestParam(name = "code", required = false) final String code) throws Exception {

        loadProperty();

        // Construct the LinkedInOAuthService instance for use
        LinkedInOAuthServiceBuilder linkedinOAuthServiceBuilder = LinkedInOAuthServiceBuilder.builder()
                .apiKey(clientId)
                .apiSecret(clientSecret)
                .scope(scope)
                .callback(redirectUri)
                .build();

        final String secretState = "secret" + new Random().nextInt(999_999);
        final String authorizationUrl = service.createAuthorizationUrlBuilder()
                .state(secretState)
                .build();
        log.info(authorizationUrl);

        RedirectView redirectView = new RedirectView();

        String userInfo = "";
        if (code != null && !code.isEmpty()) {

            log.info( "Authorization code not empty, trying to generate a 3-legged OAuth token.");

            final AccessToken[] accessToken = {
                    new AccessToken()
            };
            HttpEntity request = service.getAccessToken3Legged(code);
            String response = restTemplate.postForObject(REQUEST_TOKEN_URL, request, String.class);
            accessToken[0] = service.convertJsonTokenToPojo(response);

            prop.setProperty("token", accessToken[0].getAccessToken());
            token = accessToken[0].getAccessToken();
            refreshToken = accessToken[0].getRefreshToken();

            log.info( "Generated Access token and Refresh Token.");

            userInfo = getUserInfo(token);
            System.out.println(userInfo);
            redirectView.setUrl("/");

        } else {
            redirectView.setUrl(authorizationUrl);

        }
        return redirectView;
    }


    @GetMapping("/")
    public String message(){
      return   "Signed in";
    }




    private String getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(USERINFO_URL, HttpMethod.GET, requestEntity, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        } else {
            throw new UserException("Error accessing userinfo endpoint. Status code: " + responseEntity.getStatusCode(),HttpStatus.BAD_REQUEST);
        }
    }


    private void loadProperty() throws IOException {
        InputStream inputStream = LinkedInOAuthController.class.getClassLoader().getResourceAsStream(propFileName);
        if (inputStream != null) {
            prop.load(inputStream);
        } else {
            throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
        }
    }



}