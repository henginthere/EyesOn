package com.backend.eyeson.repository;

import com.backend.eyeson.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUserEmail(String email);

    Optional<UserEntity> findByUserSeq(long userSeq);

}
