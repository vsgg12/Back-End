package com.example.mdmggreal.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseApiRespone<T> {

    @Schema(description =  "결과 코드", example = "200")
    private String resultCd;

    @Schema(description =  "결과 메세지", example = "정상 처리되었습니다.")
    private String resultMsg;

    @Schema(description =  "결과 값")
    private T resultData;

    public BaseApiRespone() {
    }

    public BaseApiRespone(String resultCd, String resultMsg) {
        this.resultCd = resultCd;
        this.resultMsg = resultMsg;
    }

    public BaseApiRespone(String resultCd, String resultMsg, T resultData) {
        this.resultCd = resultCd;
        this.resultMsg = resultMsg;
        this.resultData = resultData;
    }

    public BaseApiRespone(HttpStatus httpStatus, String resultMsg, T resultData) {
        this.resultCd = String.valueOf(httpStatus.value());
        this.resultMsg = resultMsg;
        this.resultData = resultData;
    }

//    public BaseApiRespone(HttpStatus httpStatus) {
//        String message;
//        try {
//            message = StaticMessageSource.getMessage(Constants.MESSAGE_API + httpStatus.value());
//        } catch (NoSuchMessageException e) {
//            message = httpStatus.getReasonPhrase();
//        }
//
//        this.resultCd = String.valueOf(httpStatus.value());
//        this.resultMsg = message;
//    }

    public BaseApiRespone(HttpStatus httpStatus, T resultData) {
//        this(httpStatus);
        this.resultData = resultData;
    }

    public String getResultCd() {
        return resultCd;
    }

    public void setResultCd(String resultCd) {
        this.resultCd = resultCd;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public T getResultData() {
        return resultData;
    }

    public void setResultData(T resultData) {
        this.resultData = resultData;
    }

    @JsonIgnore
    public boolean isSuccess() {
        HttpStatus httpStatus = HttpStatus.valueOf(Objects.isNull(resultCd) ? 500 : Integer.parseInt(resultCd));
        return httpStatus.is2xxSuccessful();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
