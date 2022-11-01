package com.backend.eyeson.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="t_angel_info", schema = "eyeson")
public class AngelInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "angel_info_seq")
    private long angelInfoSeq;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="user_seq")
    private UserEntity userSeq;

    @Basic
    @Column(name = "angel_alarm_start")
    private LocalDateTime angelAlarmStart;

    @Basic
    @Column(name="angel_alarm_end")
    private LocalDateTime angelAlarmEnd;

    @Basic
    @Column(name="angel_alarm_day")
    private int angelAlarmDay;

    @Basic
    @Column(name ="angel_comp_cnt")
    private int angelCompCnt;

    @Basic
    @Column(name = "angel_help_cnt")
    private int angelHelpCnt;

    @Basic
    @Column(name = "angel_gender")
    private char angelGender;

    @Basic
    @Column(name = "angel_active")
    @ColumnDefault("false")
    private boolean angelActive;

}
