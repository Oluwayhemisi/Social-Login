package com.example.UserLogin.oauth;

import com.example.UserLogin.exceptions.UserException;
import com.example.UserLogin.oauth.builder.ScopeBuilder;
import com.example.UserLogin.oauth.pojo.AccessToken;
import com.example.UserLogin.oauth.service.LinkedInOAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.example.UserLogin.oauth.util.Constants.*;
@RestController
public final class LinkedInOAuthController {


    @Autowired
    private RestTemplate restTemplate;



    //Define all inputs in the property file
    private Properties prop = new Properties();
    private String propFileName = "application.properties";
    public static String token = null;
    public String refresh_token = null;
    public LinkedInOAuthService service;

    private Logger logger = Logger.getLogger(LinkedInOAuthController.class.getName());

    @RequestMapping(value = "/linkedinlogin")
    public String oauth(@RequestParam(name = "code", required = false) final String code) throws Exception {

        loadProperty();

        // Construct the LinkedInOAuthService instance for use
        service = new LinkedInOAuthService.LinkedInOAuthServiceBuilder()
                .apiKey(prop.getProperty("clientId"))
                .apiSecret(prop.getProperty("clientSecret"))
                .defaultScope(new ScopeBuilder(prop.getProperty("scope").split(",")).build()) // replace with desired scope
                .callback(prop.getProperty("redirectUri"))
                .build();

        final String secretState = "secret" + new Random().nextInt(999_999);
        final String authorizationUrl = service.createAuthorizationUrlBuilder()
                .state(secretState)
                .build();
        System.out.println(authorizationUrl+ "================================");

        RedirectView redirectView = new RedirectView();

        String userInfo = "";
        if (code != null && !code.isEmpty()) {
            System.out.println(code+ "+++++++++++++++++++++++++++++++++");


            logger.log(Level.INFO, "Authorization code not empty, trying to generate a 3-legged OAuth token.");

            final AccessToken[] accessToken = {
                    new AccessToken()
            };
            HttpEntity request = service.getAccessToken3Legged(code);
            String response = restTemplate.postForObject(REQUEST_TOKEN_URL, request, String.class);
            accessToken[0] = service.convertJsonTokenToPojo(response);

            prop.setProperty("token", accessToken[0].getAccessToken());
            token = accessToken[0].getAccessToken();
            refresh_token = accessToken[0].getRefreshToken();

            logger.log(Level.INFO, "Generated Access token and Refresh Token.");

             userInfo = getUserInfo(token);

        } else {
            redirectView.setUrl(authorizationUrl);

        }
        return userInfo;
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
