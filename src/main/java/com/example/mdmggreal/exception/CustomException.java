package com.example.mdmggreal.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class CustomException extends RuntimeException {
    ErrorCode errorCode;
}
