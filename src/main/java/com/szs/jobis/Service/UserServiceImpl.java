package com.szs.jobis.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.szs.jobis.Config.Provider.RefreshTokenProvider;
import com.szs.jobis.Config.Provider.TokenProvider;
import com.szs.jobis.Dto.RefundResponse;
import com.szs.jobis.Dto.ResponseAuth;
import com.szs.jobis.Dto.UserDTO;
import com.szs.jobis.Entity.ScrapEntity;
import com.szs.jobis.Entity.ScrapIrEntity;
import com.szs.jobis.Entity.ScrapPayEntity;
import com.szs.jobis.Entity.TokenEntity;
import com.szs.jobis.Entity.UserEntity;
import com.szs.jobis.Entity.UserPossibleEntity;
import com.szs.jobis.Exception.DuplicateUserException;
import com.szs.jobis.Exception.InvalidRefreshTokenException;
import com.szs.jobis.Repository.ScrapRepository;
import com.szs.jobis.Repository.initRepository;
import com.szs.jobis.Repository.tokenRepository;
import com.szs.jobis.Repository.userRepository;
import com.szs.jobis.util.AES256;


import com.szs.jobis.util.ParseCalc;
import com.szs.jobis.util.TokenStatus;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{
    private final TokenProvider tokenProvider;
    private final RefreshTokenProvider refreshTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final userRepository userRepository;
    private final tokenRepository tokenRepository;
    private final initRepository possibleRepository;

    private final ScrapRepository scrapRepository;

    private final PasswordEncoder passwordEncoder;

    //Refund
    private final String url = "https://codetest.3o3.co.kr/v2/scrap";
    private final BigDecimal T055 = new BigDecimal("0.55");
    private final BigDecimal T015 = new BigDecimal("0.15");
    private final BigDecimal P3 = new BigDecimal("3");
    private final BigDecimal P12 = new BigDecimal("12");
    private final BigDecimal P15 = new BigDecimal("15");
    private final BigDecimal P100 = new BigDecimal("100");
    private final BigDecimal P0 = new BigDecimal("0");
    private final BigDecimal 표준세액공제기준 = new BigDecimal("130000");

    @Transactional
    @Override
    public String signUp (UserDTO userDTO) throws Exception{
        JsonObject returnJson = new JsonObject();
        returnJson.addProperty("status",false);
        if (userRepository.findByUserId(userDTO.getUserId()).orElseGet(()->null) != null) {
            throw new DuplicateUserException("이미 존재하는 유저 입니다.");
        }

        Optional<UserPossibleEntity> info = possibleRepository.findByName(userDTO.getName());

        if(info.orElseGet(()->null) == null || userDTO.getRegNo().equals(AES256.decryptAES256(info.get().getRegNo())) == false ){
            throw  new DuplicateUserException("허용되지 않은 유저 입니다.");
        }

        UserEntity userEntity = UserEntity.builder()
                .userId(userDTO.getUserId())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .name(userDTO.getName())
                .regNo(AES256.encryptAES256(userDTO.getRegNo())).build();

        UserEntity save = userRepository.save(userEntity);

        returnJson.addProperty("status",true);

        return new Gson().toJson(returnJson);
    }

    @Transactional
    @Override
    public Optional<UserEntity> findByUserId(String userId) throws Exception {
        return userRepository.findByUserId(userId);
    }

    @Override
    public ResponseAuth login(String userID, String password) {
        // 받아온 유저네임과 패스워드를 이용해 UsernamePasswordAuthenticationToken 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userID, password);

        // authenticationToken 객체를 통해 Authentication 객체 생성
        // 재정의한 loadUserByUsername 메서드 호출
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 그 객체를 시큐리티 컨텍스트에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 인증 정보를 기준으로 jwt access 토큰 생성
        String accessToken = tokenProvider.createToken(authentication);

        String refreshToken = refreshTokenProvider.createToken(authentication);

        TokenEntity build = TokenEntity.builder().id(userID).refreshToken(refreshToken)
                .updateTimestamp(new Date(System.currentTimeMillis())).build();

        tokenRepository.save(build);

        return ResponseAuth.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public Optional<UserEntity> me(String token) throws Exception {
        return userRepository.findByUserId(token);
    }

    @Override
    public ResponseEntity<HashMap> scrap(String token)throws Exception {
        Optional<UserEntity> user = userRepository.findByUserId(token);
        ResponseEntity<HashMap> resultMap = null;
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> result = new HashMap<String, Object>();
        ScrapEntity scrap = new ScrapEntity();


        if(user != null){
            scrap.setUserId(token);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();

            String name = user.get().getName();
            String regNo = AES256.decryptAES256(user.get().getRegNo());

            body.add("name", name);
            body.add("regNo", regNo);

            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setReadTimeout(20000);
            factory.setConnectTimeout(20000);

            RestTemplate restTemplate = new RestTemplate(factory);

            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, header);


            UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();

            resultMap = restTemplate.postForEntity(uri.toString(),  entity , HashMap.class);

            result.put("statusCode", resultMap.getStatusCodeValue()); //http status code를 확인
            result.put("header", resultMap.getHeaders()); //헤더 정보 확인
            result.put("body", resultMap.getBody()); //실제 데이터 정보 확인

            HashMap<String, Object> bodyMap = resultMap.getBody();

            if(bodyMap != null){
                HashMap<String,Object> map = (HashMap)bodyMap.get("data");

                HashMap<String,Object> jsonList = (HashMap)map.get("jsonList");

                List<HashMap<String,Object>> 급여 = (ArrayList<HashMap<String,Object>>)jsonList.get("급여");
                List<HashMap<String,Object>> 소득공제 = (ArrayList<HashMap<String,Object>>)jsonList.get("소득공제");

                for(HashMap<String,Object> payMap : 급여){
                    scrap.getScrapPay().add(ScrapPayEntity.builder().소득내역(String.valueOf(payMap.get("소득내역"))).총지급액(ParseCalc.objToBigDecimal(payMap.get("총지급액")))
                            .업무시작일(ParseCalc.ObjToDate(payMap.get("업무시작일"))).기업명(String.valueOf(payMap.get("기업명"))).이름(String.valueOf(payMap.get("이름")))
                            .지급일(ParseCalc.ObjToDate(payMap.get("지급일"))).업무종료일(ParseCalc.ObjToDate(payMap.get("업무종료일"))).주민등록번호(AES256.encryptAES256(String.valueOf(payMap.get("주민등록번호"))))
                            .소득구분(String.valueOf(payMap.get("소득구분"))).사업자등록번호(String.valueOf(payMap.get("사업자등록번호"))).pay(scrap).build());
                }
                for(HashMap<String,Object> irMap : 소득공제){
                    scrap.getScrapIR().add(ScrapIrEntity.builder().소득구분(String.valueOf(irMap.get("소득구분"))).
                            금액(irMap.get("금액") != null? ParseCalc.objToBigDecimal(irMap.get("금액")) : ParseCalc.objToBigDecimal(irMap.get("총납임금액"))).scrap(scrap).build());
                }
                scrap.set산출세액(ParseCalc.objToBigDecimal(jsonList.get("산출세액")));
                scrap.setCompany(String.valueOf(jsonList.get("company")));
                scrap.setErrMsg(String.valueOf(jsonList.get("errMsg")));
                scrap.setSvcCd(String.valueOf(jsonList.get("svcCd")));

                scrap.setAppVer(String.valueOf(map.get("appVer")));
                scrap.setHostNm(String.valueOf(map.get("hostNm")));

                scrap.setWorkReqDt(ParseCalc.ObjToDateTZ(map.get("workerResDt")));
                scrap.setWorkResDt(ParseCalc.ObjToDateTZ(map.get("workerReqDt")));

                scrapRepository.save(scrap);
            }

            return resultMap;

        }else{
            return resultMap;
        }

    }

    @Override
    public RefundResponse refund(String token) throws Exception {
        RefundResponse res = null;
        Optional<ScrapEntity> scrap = scrapRepository.findByUserId(token);
        

        BigDecimal 결정세액 = new BigDecimal("0");
        BigDecimal 산출세액 = scrap.get().get산출세액();

        BigDecimal 근로소득세액공제금액 = 산출세액.multiply(T055);

        List<ScrapPayEntity> payList = scrap.get().getScrapPay(); // 급여

        List<ScrapIrEntity> irList = scrap.get().getScrapIR(); //소득공제

        BigDecimal 퇴직연금 = new BigDecimal("0");
        BigDecimal 의료비공제금액 =  new BigDecimal("0");
        BigDecimal 보험료공제금액 =  new BigDecimal("0");
        BigDecimal 교육비공제금액 =  new BigDecimal("0");
        BigDecimal 기부금공제금액 =  new BigDecimal("0");
        BigDecimal 총지급액 =  new BigDecimal("0");

        BigDecimal 특별세액공제금액 = new BigDecimal("0");

        BigDecimal 퇴직연금세액공제금액 =new BigDecimal("0");

        String 이름 = "";

        for(ScrapPayEntity pay : payList){
            총지급액 =  총지급액.add(pay.get총지급액());
            이름 = pay.get이름();
        }


        for(ScrapIrEntity ir : irList){
            String section = ir.get소득구분();
            if(section.equals("퇴직연금")){
                퇴직연금 = ir.get금액();
                퇴직연금세액공제금액 = (퇴직연금.multiply(T015));
            }else if(section.equals("의료비")){
                BigDecimal temp = 총지급액.multiply(P3).divide(P100);
                의료비공제금액 = ir.get금액().subtract(temp).multiply(P15).divide(P100);
                의료비공제금액 = 의료비공제금액.compareTo(P0) == -1 ? P0 : 의료비공제금액;
                특별세액공제금액 = 특별세액공제금액.add(의료비공제금액);
            }else if(section.equals("교육비")){
                교육비공제금액 = ir.get금액().multiply(P15).divide(P100);
                특별세액공제금액 = 특별세액공제금액.add(교육비공제금액);
            }else if(section.equals("보험료")){
                보험료공제금액 = ir.get금액().multiply(P12).divide(P100);
                특별세액공제금액 = 특별세액공제금액.add(보험료공제금액);
            }else if(section.equals("기부금")){
                기부금공제금액 =  ir.get금액().multiply(P15).divide(P100);
                특별세액공제금액 = 특별세액공제금액.add(기부금공제금액);
            }
        }

        BigDecimal 표준세액공제금액 = new BigDecimal("0");
        if(특별세액공제금액.compareTo(표준세액공제기준) == -1){
            표준세액공제금액 = 표준세액공제기준;
            특별세액공제금액 = P0;
        }

        결정세액 = 산출세액.subtract(근로소득세액공제금액).subtract(특별세액공제금액).subtract(표준세액공제금액).subtract(퇴직연금세액공제금액);

        res = RefundResponse.builder().이름(이름).결정세액(ParseCalc.BigDecimalToString(결정세액.compareTo(P0) == -1 ? P0 : 결정세액))
                .퇴직연금세액공제(ParseCalc.BigDecimalToString(퇴직연금세액공제금액)).build();
        return res;
    }

}
