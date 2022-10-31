package com.backend.eyeson.repository;

import com.backend.eyeson.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> getByUserEmail(String userEmail);

    Optional<UserEntity> findByUserEmail(String email);

    UserEntity findByUserSeq(long userSeq);

}
