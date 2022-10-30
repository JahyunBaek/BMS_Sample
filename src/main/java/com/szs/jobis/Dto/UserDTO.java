package com.szs.jobis.Dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserDTO {

    @NotBlank(message = "아이디를 입력하여 주십시오.")
    @Size(min = 4, max = 12, message = "아이디는 4글자 이상, 12글자 이하로 입력하여 주십시오.")
    @ApiModelProperty(example = "test")
    public String userId;

    @NotBlank(message = "비밀번호를 입력하여 주십시오.")
    @Size(min = 8, max = 20, message = "비밀번호는 8글자 이상, 20글자 이하로 입력하여 주십시오.")
    @ApiModelProperty(example = "1q2w3e4r")
    public String password;

    @NotBlank(message = "이름을 입력하여 주십시오.")
    @Size(min = 2, max = 10, message = "이름은 2글자 이상, 10글자 이하로 입력하여 주십시오.")
    @ApiModelProperty(example = "홍길동")
    public String name;

    @NotBlank(message = "주민번호를 입력하여 주십시오.")
    @Size(min = 14, max = 14, message = "주민번호 앞자리, 뒷자리, '-' 를 포함하여 총 14자리를 입력하여 주십시오.")
    @ApiModelProperty(example = "860824-1655068")
    public String regNo;

    @Builder
    public UserDTO(String userId, String password, String name, String regNo){
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.regNo = regNo;
    }
    public UserDTO(){

    }
}
