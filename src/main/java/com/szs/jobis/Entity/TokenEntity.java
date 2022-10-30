package com.szs.jobis.Entity;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name="TOKEN")
@Entity
public class TokenEntity {

    @Id
    @NotNull
    private String id;

    @NotNull
    private  String refreshToken;

    private Date updateTimestamp;

    @Builder
    public TokenEntity(String id, String refreshToken, Date updateTimestamp) {
        this.id = id;
        this.refreshToken = refreshToken;
        this.updateTimestamp = updateTimestamp;
    }

    public TokenEntity() {

    }
}
