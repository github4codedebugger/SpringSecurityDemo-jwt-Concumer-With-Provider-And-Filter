package com.ss.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRegistrationRequest {

    private String userId;

    private String userName;

    private String email;

    private String mobile;

    private String address;

    private String password;
}
