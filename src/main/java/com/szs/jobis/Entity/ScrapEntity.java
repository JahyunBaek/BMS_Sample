package com.szs.jobis.Entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "SCRAP")
@Data
public class ScrapEntity {


    @Id
    @NotNull
    @Column(name = "scrap_id")
    private String userId;



    private String 산출세액;
    private String errMsg;
    private String company;
    private String svcCd;

    //@JoinColumn(name = "IR_ID", insertable = false, updatable = false)

    @OneToMany(mappedBy = "pay",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScrapPayEntity> scrapPay = new ArrayList<>();

    @OneToMany(mappedBy = "scrap",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScrapIrEntity> scrapIR = new ArrayList<>();
}