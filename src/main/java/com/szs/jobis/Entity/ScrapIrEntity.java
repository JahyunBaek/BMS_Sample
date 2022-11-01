package com.szs.jobis.Entity;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;
@Entity
@Table(name = "SCRAPIR")
@Data
public class ScrapIrEntity {
    @Id
    @GeneratedValue
    @Column(name = "IR_ID")
    private Long id;
    
    private String 금액;
    private String 소득구분;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scrap_id")
    ScrapEntity scrap;
}