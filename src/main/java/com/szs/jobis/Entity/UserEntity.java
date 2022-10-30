package com.szs.jobis.Entity;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USERS")
@Data
public class UserEntity {
    @Id
    @NotNull
    private String userId;

    @NotNull
    private String password;

    @NotNull
    private String name;

    @NotNull
    private String regNo;
    @Builder
    public UserEntity(String userId, String password, String name, String regNo) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.regNo = regNo;
    }

    public UserEntity() {

    }
}
