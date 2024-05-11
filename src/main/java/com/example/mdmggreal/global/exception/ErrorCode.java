package com.example.mdmggreal.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    // 유저
    INVALID_USER_ID(HttpStatus.NOT_FOUND, "존재하지 않는 아이디입니다."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디입니다."),
    NICKNAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),

    // 이미지
    NOT_FOUND_IMAGE(HttpStatus.BAD_REQUEST, "이미지가 존재하지 않습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
    }
