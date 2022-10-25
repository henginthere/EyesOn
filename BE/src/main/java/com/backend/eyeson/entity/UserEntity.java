package com.backend.eyeson.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
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
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_seq")
    private long userSeq;

    @Basic
    @Column(name="user_name", length = 5, nullable = false)
    private String userName;

    @Basic
    @Column(name="user_email", length = 30, nullable = false)
    private String userEmail;

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
