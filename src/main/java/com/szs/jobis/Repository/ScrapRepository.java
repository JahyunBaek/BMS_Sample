package com.szs.jobis.Repository;

import com.szs.jobis.Entity.ScrapEntity;
import com.szs.jobis.Entity.UserPossibleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScrapRepository extends JpaRepository<ScrapEntity,String>{
    Optional<ScrapEntity> findByUserId(String UserId);
}