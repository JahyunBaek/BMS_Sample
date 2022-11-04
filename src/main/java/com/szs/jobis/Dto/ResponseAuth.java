package com.szs.jobis.Dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseAuth {
    @ApiModelProperty(example = "Access Token Info")
    private String accessToken;
    @ApiModelProperty(example = "Refresh Token Info")
    private String refreshToken;
}