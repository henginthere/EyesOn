package com.backend.eyeson.repository;

import com.backend.eyeson.entity.AngelInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AngelRepository extends JpaRepository<AngelInfoEntity, Long> {

    Optional<AngelInfoEntity> findByUserSeq(long userSeq);
}
