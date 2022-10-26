package com.backend.eyeson.dto;

import com.backend.eyeson.entity.CompStateEnum;
import com.backend.eyeson.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintsDto {

    private long compSeq;
    private UserEntity blindUser;
    private UserEntity angelUser;
    private CompStateEnum compState;
    private String compReturn;
    private String compAddress;
    private String compImage;
    private String compTitle;
    private String compContent;
    private LocalDateTime compRegTime;
    private String compResultContent;
}
