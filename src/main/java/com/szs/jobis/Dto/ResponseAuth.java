package com.szs.jobis.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public class ResponseAuth {
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    //@JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Token {
        private String accessToken;
        private String refreshToken;
    }
}
