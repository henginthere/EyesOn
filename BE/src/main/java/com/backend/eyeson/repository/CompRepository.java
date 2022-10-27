package com.backend.eyeson.repository;

import com.backend.eyeson.entity.ComplaintsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompRepository extends JpaRepository<ComplaintsEntity, Long> {


}
