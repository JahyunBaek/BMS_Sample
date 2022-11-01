package com.szs.jobis.Service;

import com.szs.jobis.Dto.ResponseAuth;
import com.szs.jobis.Dto.UserDTO;
import com.szs.jobis.Entity.ScrapEntity;
import com.szs.jobis.Entity.UserEntity;

import java.util.Optional;

public interface UserService {
    String signUp (UserDTO userDTO) throws Exception;
    Optional<UserEntity> findByUserId(String userId) throws Exception;
    ResponseAuth login(String username, String password) throws Exception;
    ResponseAuth refresh(String refreshToken) throws Exception;
    Optional<UserEntity> me(String token) throws Exception;
    ScrapEntity scrap(String token) throws Exception;
    Optional<ScrapEntity> scrap2(String token) throws Exception;
}
