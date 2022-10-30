package com.szs.jobis.Controller;

import com.szs.jobis.Dto.ResponseAuth;
import com.szs.jobis.Dto.UserDTO;
import com.szs.jobis.Service.UserServiceImpl;
import com.szs.jobis.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/szs")
public class CommonController {

    private final UserServiceImpl userService;

    @PostMapping(value = "/signup", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> SignUp(@Valid @RequestBody UserDTO userDTO) throws Exception {
        return ResponseEntity.ok(userService.signUp(userDTO));
    }

    @PostMapping(value = "/login", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> Login(@Valid @RequestBody UserDTO memberDTO) throws Exception {
        ResponseAuth.Token token = userService.login(memberDTO.getUserId(), memberDTO.getPassword());
        // response header 에도 넣고 응답 객체에도 넣는다.
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + token.getAccessToken());

        return ResponseEntity.ok(token);
    }

    @PostMapping(value = "/refresh", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> Refresh(@Valid @RequestBody UserDTO memberDTO) throws Exception {
        return ResponseEntity.ok(userService.signUp(memberDTO));
    }
}
