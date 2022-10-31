package com.szs.jobis.Entity;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USERPOSSIBLE")
@Data
public class UserPossibleEntity {
    @Id
    @NotNull
    private String userId;

    @NotNull
    private String regNo;
    @Builder
    public UserPossibleEntity(String userId,  String regNo) {
        this.userId = userId;
        this.regNo = regNo;
    }

    public UserPossibleEntity() {

    }
}
