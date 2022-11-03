package com.szs.jobis.security;


import com.szs.jobis.Entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class UserAdapter extends User {
    private UserEntity userEntity;

    public UserAdapter(UserEntity userEntity, Collection authorities) {
        super(userEntity.getUserId(), userEntity.getPassword(), authorities);
        this.userEntity = userEntity;
    }

    public UserEntity getAccount() {
        return this.userEntity;
    }
}
