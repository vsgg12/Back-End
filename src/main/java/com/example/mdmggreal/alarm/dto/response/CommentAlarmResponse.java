package com.example.mdmggreal.alarm.dto.response;

import com.example.mdmggreal.alarm.dto.CommentAlarmDTO;
import com.example.mdmggreal.global.response.BaseResponse;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.util.List;


@Getter
@SuperBuilder
public class CommentAlarmResponse extends BaseResponse {
    private List<CommentAlarmDTO> commentAlarmList;

    public static CommentAlarmResponse from(List<CommentAlarmDTO> commentAlarmDTOList, HttpStatus status) {
        return CommentAlarmResponse.builder()
                .resultCode(status.value())
                .resultMsg(status.name())
                .commentAlarmList(commentAlarmDTOList)
                .build();
    }
}
