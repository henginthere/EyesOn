package com.backend.eyeson.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="t_user", schema = "eyeson")
public class UserEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_seq")
    private long userSeq;

    @Basic
    @Column(name="user_email", length = 30, nullable = false)
    private String userEmail;

    @Basic
    @JsonIgnore
    // 쓰기 전용 및 조회 불가
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "user_pass", length = 100)
    private String userPass;

    @Basic
    @Column(name="user_gender", length = 1, nullable = false)
    private char userGender;

    @Basic
    @Column(name="user_fcm", length = 300, nullable = false)
    private String userFcm;

    @Basic
    @CreatedDate
    @Column(name="user_date", nullable = false)
    private LocalDateTime userDate;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "user_authority")
    private AuthorityEntity authority;
}
