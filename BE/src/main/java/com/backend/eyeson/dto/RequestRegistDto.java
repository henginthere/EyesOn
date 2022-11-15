package com.backend.eyeson.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestRegistDto {
    private String userName;
    private String userEmail;
    private String userGender;
    private String userFcm;
    private boolean userRole;
}
