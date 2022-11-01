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
    private String name;

    @NotNull
    private String regNo;
    @Builder
    public UserPossibleEntity(String name,  String regNo) {
        this.name = name;
        this.regNo = regNo;
    }

    public UserPossibleEntity() {

    }
}
