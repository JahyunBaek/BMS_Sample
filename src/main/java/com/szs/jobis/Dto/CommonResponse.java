package com.szs.jobis.Dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.UUID;

@Getter
@Builder
public class CommonResponse {
    @Builder.Default
    private String id = UUID.randomUUID().toString(); // uuid
    @Builder.Default
    private Date dateTime = new Date(); // date
    private Boolean success;
    private Object response;
    private Object error;
}