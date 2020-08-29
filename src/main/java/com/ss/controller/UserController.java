package com.ss.controller;

import com.ss.config.ConfigUtils;
import com.ss.model.AuthToken;
import com.ss.model.CustomerRegistrationRequest;
import com.ss.model.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/customers")
public class UserController {

    @Autowired
    private ConfigUtils configUtils;

    private RestTemplate restTemplate;

    @Autowired
    public UserController(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthToken register(@RequestBody CustomerRegistrationRequest user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, configUtils.getBasicAuth());

        HttpEntity<CustomerRegistrationRequest> entity = new HttpEntity<>(user, headers);

        return restTemplate.exchange(configUtils.getRegistrationUrl(), HttpMethod.POST, entity, AuthToken.class).getBody();
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthToken login(@RequestBody LoginRequest loginDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, configUtils.getBasicAuth());
        HttpEntity<LoginRequest> entity = new HttpEntity<>(loginDto, headers);
        return restTemplate.exchange(configUtils.getLoginUrl(), HttpMethod.POST, entity, AuthToken.class).getBody();
    }

    @GetMapping("/wallet-balance")
    @ResponseStatus(HttpStatus.OK)
    public double getWalletBalance() {
        // dummy logic
        return 200.00;
    }

}
