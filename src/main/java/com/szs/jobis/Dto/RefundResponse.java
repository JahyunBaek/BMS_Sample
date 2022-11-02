package com.szs.jobis.Dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RefundResponse {
    private String 이름;
    private String 결정세액;
    private String 퇴직연금세액공제;
}
