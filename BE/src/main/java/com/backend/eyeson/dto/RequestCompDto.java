package com.backend.eyeson.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestCompDto {
    private long compSeq;
    private String compAddress;
    private String compImage;
    private String compContent;
    private String compReturn;
    private String compTitle;
    private String compResultContent;
}
