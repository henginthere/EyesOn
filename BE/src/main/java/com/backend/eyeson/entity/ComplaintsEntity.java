package com.backend.eyeson.entity;

import java.time.LocalDateTime;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="t_complaints", schema = "eyeson")
public class ComplaintsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comp_seq")
    private long compSeq;

    @ManyToOne
    @JoinColumn(name = "blind_seq")
    private UserEntity blindUser;

    @ManyToOne
    @JoinColumn(name = "angel_seq")
    private UserEntity angelUser;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "comp_state")
    private CompStateEnum compState;

    @Basic
    @Column(name = "comp_return")
    private String compReturn;

    @Basic
    @Column(name = "comp_address", length = 256)
    private String compAddress;

    @Basic
    @Column(name = "comp_image", length = 256)
    private String compImage;

    @Basic
    @Column(name = "comp_title", length = 100)
    private String compTitle;

    @Basic
    @Column(name = "comp_content")
    private String compContent;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @Basic
    @CreatedDate
    @Column(name = "comp_regtime")
    private LocalDateTime compRegtime;

    @Basic
    @Column(name = "comp_result_content")
    private String compResultContent;
}
