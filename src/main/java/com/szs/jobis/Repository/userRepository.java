package com.szs.jobis.Repository;

import com.szs.jobis.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface userRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByUserId(String userId);
}
