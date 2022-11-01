package com.szs.jobis.Repository;

import com.szs.jobis.Entity.UserPossibleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface initRepository extends JpaRepository<UserPossibleEntity,String> {
    Optional<UserPossibleEntity> findByName(String Name);
    Optional<UserPossibleEntity> findByNameAndRegNo(String userId, String regNo);
}
