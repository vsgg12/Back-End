package com.example.mdmggreal.oauth.dto;

import lombok.Getter;

@Getter
public class SignInRequest {

    private String code;

    private String state;
}
