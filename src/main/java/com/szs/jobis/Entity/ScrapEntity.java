package com.szs.jobis.Entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Builder;
import lombok.Data;

@Entity
@Table
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
public class ScrapEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="sid")
    private Long id;

    @Column
    private String userId;

    private BigDecimal 산출세액;
    private String errMsg;
    private String company;
    private String svcCd;

    private String appVer;
    private String hostNm;

    //@JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy.MM.dd HH:mm:ss", timezone="Asia/Seoul")
    private Date workResDt;
    //@JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy.MM.dd HH:mm:ss", timezone="Asia/Seoul")
    private Date workReqDt;

    @OneToMany(mappedBy = "pay",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScrapPayEntity> scrapPay = new ArrayList<>();

    @OneToMany(mappedBy = "scrap",  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScrapIrEntity> scrapIR = new ArrayList<>();

    @Builder
    public ScrapEntity(String userId,BigDecimal 산출세액,String errMsg,String company,String svcCd,List<ScrapPayEntity> scrapPay,
                       List<ScrapIrEntity> scrapIR,String appVer,String hostNm,Date workResDt,Date workReqDt){
        this.userId = userId;
        this.산출세액 = 산출세액;
        this.errMsg = errMsg;
        this.company = company;
        this.svcCd = svcCd;
        this.scrapPay = scrapPay;
        this.scrapIR = scrapIR;
        this.appVer = appVer;
        this.hostNm = hostNm;
        this.workResDt = workResDt;
        this.workReqDt = workReqDt;
    }
    public ScrapEntity(){}
}