package com.backend.eyeson.repository;

import com.backend.eyeson.dto.ComplaintsDto;
import com.backend.eyeson.entity.CompStateEnum;
import com.backend.eyeson.entity.ComplaintsEntity;
import com.backend.eyeson.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface CompRepository extends JpaRepository<ComplaintsEntity, Long> {
    Page<ComplaintsEntity> findAllByCompStateOrderByCompSeqAsc(CompStateEnum stateEnum, Pageable pageable);
    Page<ComplaintsEntity> findAllByAngelUserOrderByCompSeqAsc(UserEntity angelUser, Pageable pageable);
    Page<ComplaintsEntity> findAllByBlindUserOrderByCompSeqAsc(UserEntity blindUser, Pageable pageable);

    Optional<ComplaintsEntity> findByCompSeq(long compSeq);

    @Query(value = "SELECT count(*) FROM t_complaints WHERE comp_state is TRUE", nativeQuery = true)
    public int entire();


}
