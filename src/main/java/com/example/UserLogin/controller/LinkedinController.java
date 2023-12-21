package com.example.UserLogin.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/auth/linkedin/")
@RequiredArgsConstructor
public class LinkedinController {

    String id = "";
    String secret = "";
    String url = "";
    String redirect = "";

    public Object accessToken(@RequestParam("code") String code){
        RestTemplate restTemplate = new RestTemplate();
        Map<String , String> map = new HashMap<>();
        String s = "https://www.linkedin.com/oauth/v2/"+"accessToken?grant_type=authorization_code&code="
                +code+"&redirect_uri="+redirect+"&client_id="+id+"&client_secret="+secret;

        try{

            HttpHeaders headers =  createHttpHeaders();
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(map,headers);
            ResponseEntity<Object> response = restTemplate.exchange(s, HttpMethod.POST, entity, Object.class);
            System.out.println(response.getBody());
            return response.getBody();

        }catch (Exception ex){
            System.out.println("Exception: "+ ex.getMessage());
        }
        return null;
    }

    private HttpHeaders createHttpHeaders(){
        HttpHeaders headers = new HttpHeaders();
        return headers;
    }

//    public Builder getBuilder()
}
