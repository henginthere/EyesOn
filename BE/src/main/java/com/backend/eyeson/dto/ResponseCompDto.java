package com.backend.eyeson.dto;

import com.backend.eyeson.entity.CompStateEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseCompDto {
    private long compSeq;
    private String compAddress;
    private String compContent;
    private String compImage;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime compRegtime;
    private String compResultContent;
    private String compReturn;
    private CompStateEnum compState;
    private String compTitle;

}
