package com.backend.eyeson.dto;

import com.backend.eyeson.entity.CompStateEnum;
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
    private LocalDateTime compRegtime;
    private String compResultContent;
    private String compReturn;
    private CompStateEnum compState;
    private String compTitle;

}
