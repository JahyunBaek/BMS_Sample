package com.szs.jobis.Repository;

import com.szs.jobis.Entity.ScrapEntity;
import com.szs.jobis.Entity.ScrapPayEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScrapPayRepository extends JpaRepository<ScrapPayEntity,String>{
    //Optional<ScrapPayEntity> findByUserId(String UserId);
}