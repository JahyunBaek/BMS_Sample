package com.szs.jobis.Entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table
@Data
public class ScrapPayEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PAY_ID")
    private Long id;

    private String 소득내역;
    private BigDecimal 총지급액;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy.MM.dd", timezone="Asia/Seoul")
    private Date 업무시작일;
    private String 기업명;
    private String 이름;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy.MM.dd", timezone="Asia/Seoul")
    private Date 지급일;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy.MM.dd", timezone="Asia/Seoul")
    private Date 업무종료일;

    private String 주민등록번호;
    private String 소득구분;
    private String 사업자등록번호;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sid")
    private ScrapEntity pay;

    @Builder
    public ScrapPayEntity(String 소득내역,BigDecimal 총지급액,Date 업무시작일,String 기업명,
                          String 이름,Date 지급일,Date 업무종료일,String 주민등록번호,String 소득구분,String 사업자등록번호,ScrapEntity pay){
        this.소득내역 = 소득내역;
        this.총지급액 = 총지급액;
        this.업무시작일 = 업무시작일;
        this.기업명 = 기업명;
        this.이름 = 이름;
        this.지급일 = 지급일;
        this.업무종료일 = 업무종료일;
        this.주민등록번호 = 주민등록번호;
        this.소득구분 = 소득구분;
        this.사업자등록번호 = 사업자등록번호;
        this.pay = pay;
    }

    public ScrapPayEntity() {

    }
}