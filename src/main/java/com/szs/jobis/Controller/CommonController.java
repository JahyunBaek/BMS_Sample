package com.szs.jobis.Controller;

import com.szs.jobis.Dto.ResponseAuth;
import com.szs.jobis.Dto.UserDTO;
import com.szs.jobis.Service.UserServiceImpl;
import com.szs.jobis.security.JwtFilter;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/szs")
public class CommonController {

    private final UserServiceImpl userService;

    @ApiOperation(value = "사용자 정보등록 API" , notes = "특정 사용자만 가입이 가능합니다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK !!"),
        @ApiResponse(code = 500, message = "Internal Server Error !!"),
        @ApiResponse(code = 404, message = "Not Found !!")
    })
    @PostMapping(value = "/signup",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> SignUp(@Valid @RequestBody UserDTO userDTO) throws Exception {
        return ResponseEntity.ok(userService.signUp(userDTO));
    }

    @ApiOperation(value = "로그인 API" , notes = "로그인을 통하여 토큰을 발급 받습니다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK !!"),
        @ApiResponse(code = 500, message = "Internal Server Error !!"),
        @ApiResponse(code = 404, message = "Not Found !!")
    })
    @PostMapping(value = "/login",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> Login(@Valid @RequestBody UserDTO memberDTO) throws Exception {
        ResponseAuth token = userService.login(memberDTO.getUserId(), memberDTO.getPassword());
        // response header 에도 넣고 응답 객체에도 넣는다.
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + token.getAccessToken());
        httpHeaders.add(JwtFilter.REFRESH_HEADER, "Bearer " + token.getRefreshToken());

        return ResponseEntity.ok().headers(httpHeaders).body(memberDTO);
    }
    
    @ApiOperation(value = "회원정보 조회 API" , notes = "가입한 회원 정보를 가져오는 API")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK !!"),
        @ApiResponse(code = 500, message = "Internal Server Error !!"),
        @ApiResponse(code = 404, message = "Not Found !!")
    })
    @GetMapping(value = "/me",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> Me(@AuthenticationPrincipal User user) throws Exception {
        return ResponseEntity.ok(userService.me(user.getUsername()));
    }
    
    @ApiOperation(value = "SCRAP API" , notes = "가입한 유저의 정보를 스크랩 합니다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK !!"),
        @ApiResponse(code = 500, message = "Internal Server Error !!"),
        @ApiResponse(code = 404, message = "Not Found !!")
    })
    @PostMapping(value = "/scrap",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> Scrap(@AuthenticationPrincipal User user) throws Exception {
        return ResponseEntity.ok(userService.scrap(user.getUsername()));
    }

    @ApiOperation(value = "Refund API" , notes = "유저의 스크랩 정보를 바탕으로 유저의 결정세액과 퇴직연금세액공제금액을 계산합니다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK !!"),
        @ApiResponse(code = 500, message = "Internal Server Error !!"),
        @ApiResponse(code = 404, message = "Not Found !!")
    })
    @GetMapping(value = "/refund",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> Refund(@AuthenticationPrincipal User user) throws Exception {
        return ResponseEntity.ok(userService.refund(user.getUsername()));
    }
}