package com.backend.eyeson.dto;

import com.backend.eyeson.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Basic;
import javax.persistence.Column;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private long userSeq;
    private String userEmail;
    private char userGender;
    private String userFcm;
    private LocalDateTime userDate;

    public static UserDto of(UserEntity user) {
        if (user == null) {
            return null;
        }
        return new UserDtoBuilder() //
                .userSeq(user.getUserSeq()) //
                .userEmail(user.getUserEmail())
                .userGender(user.getUserGender())
                .userFcm(user.getUserFcm())
                .userDate(user.getUserDate())
                .build();
    }
}
