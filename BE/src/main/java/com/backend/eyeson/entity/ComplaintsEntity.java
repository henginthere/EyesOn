package com.backend.eyeson.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
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
@Table(name="t_complaints", schema = "eyeson")
public class ComplaintsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comp_seq")
    private long compSeq;

    @ManyToOne
    @JoinColumn(name = "blind_seq", insertable = false, updatable = false)
    private UserEntity blindUser;

    @ManyToOne
    @JoinColumn(name = "angel_seq", insertable = false, updatable = false)
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

    @Basic
    @CreatedDate
    @Column(name = "comp_regtime")
    private LocalDateTime compRegtime;

    @Basic
    @Column(name = "comp_result_content")
    private String compResultContent;
}
