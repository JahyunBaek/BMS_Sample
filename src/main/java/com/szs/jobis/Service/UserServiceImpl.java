package com.szs.jobis.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.szs.jobis.Config.Provider.RefreshTokenProvider;
import com.szs.jobis.Config.Provider.TokenProvider;
import com.szs.jobis.Dto.ResponseAuth;
import com.szs.jobis.Dto.UserDTO;
import com.szs.jobis.Entity.TokenEntity;
import com.szs.jobis.Entity.UserEntity;
import com.szs.jobis.Exception.DuplicateUserException;
import com.szs.jobis.Exception.InvalidRefreshTokenException;
import com.szs.jobis.Repository.tokenRepository;
import com.szs.jobis.Repository.userRepository;
import com.szs.jobis.security.UserAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{
    private final TokenProvider tokenProvider;
    private final RefreshTokenProvider refreshTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final userRepository userRepository;
    private final tokenRepository tokenRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public String signUp (UserDTO userDTO) throws Exception{
        JsonObject returnJson = new JsonObject();

        if (userRepository.findByUserId(userDTO.getUserId()).orElseGet(()->null) != null) {
            throw new DuplicateUserException("이미 존재하는 유저 입니다.");
        }
        UserEntity userEntity = UserEntity.builder()
                .userId(userDTO.getUserId())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .name(userDTO.getName())
                .regNo(passwordEncoder.encode(userDTO.getRegNo())).build();

        returnJson.addProperty("status",true);

        return new Gson().toJson(returnJson);
    }

    @Transactional
    @Override
    public Optional<UserEntity> findByUserId(String userId) throws Exception {
        return userRepository.findByUserId(userId);
    }

    @Override
    public ResponseAuth.Token login(String userID, String password) {
        // 받아온 유저네임과 패스워드를 이용해 UsernamePasswordAuthenticationToken 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userID, password);

        // authenticationToken 객체를 통해 Authentication 객체 생성
        // 이 과정에서 CustomUserDetailsService 에서 우리가 재정의한 loadUserByUsername 메서드 호출
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 그 객체를 시큐리티 컨텍스트에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 인증 정보를 기준으로 jwt access 토큰 생성
        String accessToken = tokenProvider.createToken(authentication);

        // 위에서 loadUserByUsername를 호출하였으므로 AccountAdapter가 시큐리티 컨텍스트에 저장되어 Account 엔티티 정보를 우리는 알 수 있음
        // 유저 정보에서 중치를 꺼내 리프레시 토큰 가중치에 할당, 나중에 액세스토큰 재발급 시도 시 유저정보 가중치 > 리프레시 토큰이라면 실패
        //Long tokenWeight = ((UserAdapter)authentication.getPrincipal()).getAccount().getTokenWeight();
        String refreshToken = refreshTokenProvider.createToken(authentication);

        TokenEntity build = TokenEntity.builder().id(userID).refreshToken(refreshToken)
                .updateTimestamp(new Date(System.currentTimeMillis())).build();

        tokenRepository.save(build);

        return ResponseAuth.Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    @Override
    public ResponseAuth.Token refresh(String refreshToken) throws Exception {
        if(!refreshTokenProvider.validateToken(refreshToken)) throw new InvalidRefreshTokenException();
        // 리프레시 토큰 값을 이용해 사용자를 꺼낸다.

        Authentication authentication = refreshTokenProvider.getAuthentication(refreshToken);
        UserEntity userEntity = userRepository.findByUserId(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException(authentication.getName() + "을 찾을 수 없습니다"));

        // 리프레시 토큰에 담긴 값을 그대로 액세스 토큰 생성에 활용한다.
        String accessToken = tokenProvider.createToken(authentication);
        // 기존 리프레시 토큰과 새로 만든 액세스 토큰을 반환한다.
        return ResponseAuth.Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
