package com.backend.eyeson.repository;

import com.backend.eyeson.entity.AngelInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AngelRepository extends JpaRepository<AngelInfoEntity, Long> {

    Optional<AngelInfoEntity> findByUserEntity_UserSeq(long userSeq);

    Optional<List<AngelInfoEntity>> findAllByAngelGender(char gender);
    List<AngelInfoEntity> findAll();
}
