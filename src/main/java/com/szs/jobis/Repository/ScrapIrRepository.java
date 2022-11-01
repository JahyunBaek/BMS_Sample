package com.szs.jobis.Repository;

import com.szs.jobis.Entity.ScrapEntity;
import com.szs.jobis.Entity.ScrapIrEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScrapIrRepository extends JpaRepository<ScrapIrEntity,String>{
   //Optional<ScrapIrEntity> findByUserId(String UserId);
}