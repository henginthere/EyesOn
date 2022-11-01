package com.backend.eyeson.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestAngelInfoDto {

    private int angelAlarmStart;
    private int angelAlarmEnd;
    private int angelAlarmDay;
    private boolean angelActive;

}
