package com.backend.eyeson.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

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

    @ManyToMany // user와 authority 다대다 관계를 일대다, 다대일 관계의 조인테이블로 정의
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "userEmail", referencedColumnName = "user_email")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<AuthorityEntity> authorities;
}
