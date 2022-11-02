package com.szs.jobis.Service;

import com.szs.jobis.Dto.RefundResponse;
import com.szs.jobis.Dto.ResponseAuth;
import com.szs.jobis.Dto.UserDTO;
import com.szs.jobis.Entity.ScrapEntity;
import com.szs.jobis.Entity.UserEntity;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

public interface UserService {
    String signUp (UserDTO userDTO) throws Exception;
    Optional<UserEntity> findByUserId(String userId) throws Exception;
    ResponseAuth login(String username, String password) throws Exception;
    ResponseAuth refresh(String refreshToken) throws Exception;
    Optional<UserEntity> me(String token) throws Exception;
    ResponseEntity<HashMap> scrap(String token) throws Exception;
    RefundResponse refund(String token) throws Exception;
}
