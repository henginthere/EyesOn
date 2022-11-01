package com.backend.eyeson.repository;

import com.backend.eyeson.entity.CompStateEnum;
import com.backend.eyeson.entity.ComplaintsEntity;
import com.backend.eyeson.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface CompRepository extends JpaRepository<ComplaintsEntity, Long> {
    Optional<ComplaintsEntity> findByAngelUser(UserEntity angelUser);
    Page<ComplaintsEntity> findAllByCompStateOrderByCompSeq(CompStateEnum stateEnum, Pageable pageable);


}
