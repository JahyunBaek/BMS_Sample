package com.szs.jobis.Entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "SCRAPPAY")
@Data
public class ScrapPayEntity {
    @Id
    @GeneratedValue
    @Column(name = "PAY_ID")
    private Long id;

    private String 소득내역;
    private String 총지급액;
    private String 업무시작일;
    private String 기업명;
    private String 이름;

    private Date 지급일;
    private Date 업무종료일;

    private String 주민등록번호;
    private String 소득구분;
    private String 사업자등록번호;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scrap_id"/*, insertable = false, updatable = false*/)
    ScrapEntity pay;
}
