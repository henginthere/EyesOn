package com.backend.eyeson.dto;

import com.backend.eyeson.entity.CompStateEnum;
import com.backend.eyeson.entity.ComplaintsEntity;
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
    private LocalDateTime compRegtime;
    private String compResultContent;

    public ComplaintsDto(ComplaintsEntity complaintsEntity) {
        this.compSeq = complaintsEntity.getCompSeq();
        this.blindUser = complaintsEntity.getBlindUser();
        this.angelUser = complaintsEntity.getAngelUser();
        this.compState = complaintsEntity.getCompState();
        this.compReturn = complaintsEntity.getCompReturn();
        this.compAddress = complaintsEntity.getCompAddress();
        this.compImage = complaintsEntity.getCompImage();
        this.compTitle = complaintsEntity.getCompTitle();
        this.compContent = complaintsEntity.getCompContent();
        this.compRegtime = complaintsEntity.getCompRegtime();
        this.compResultContent = complaintsEntity.getCompResultContent();
    }
}
