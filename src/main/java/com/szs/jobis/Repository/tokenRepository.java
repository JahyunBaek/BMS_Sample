package com.szs.jobis.Repository;

import com.szs.jobis.Entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface tokenRepository extends JpaRepository<TokenEntity, String> {
}
