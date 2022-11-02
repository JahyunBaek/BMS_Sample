package com.szs.jobis.Entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;
@Entity
@Table
@Data
public class ScrapIrEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "IR_ID")
    private Long id;

    private BigDecimal 금액;
    private String 소득구분;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sid")
    private ScrapEntity scrap;

    @Builder
    public ScrapIrEntity(BigDecimal 금액,String 소득구분,ScrapEntity scrap){
        this.금액 = 금액;
        this.소득구분 = 소득구분;
        this.scrap = scrap;
    }

    public ScrapIrEntity() {

    }
}