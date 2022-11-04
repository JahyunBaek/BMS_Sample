package com.szs.jobis.Config.Provider;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.szs.jobis.Entity.UserEntity;
import com.szs.jobis.Repository.userRepository;

import java.util.Date;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

public class RefreshTokenProvider extends TokenProvider {

    
    private static userRepository userRepository; 

    public RefreshTokenProvider(String secret, long tokenValidityInSeconds) {
        super(secret, tokenValidityInSeconds);
    }

    // 토큰 생성
    public String createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = System.currentTimeMillis();
        Date IssuedAtDate = new Date(now);
        Date ExpirationDate = new Date(now + this.tokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setIssuedAt(IssuedAtDate)
                .setExpiration(ExpirationDate)
                .compact();
    }

    @Transactional
    public String reissueRefreshToken(String refreshToken) throws RuntimeException{

        Authentication authentication = this.getAuthentication(refreshToken);
        UserEntity userEntity = userRepository.findByUserId(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException(authentication.getName() + "을 찾을 수 없습니다"));

        // 리프레시 토큰에 담긴 값을 그대로 액세스 토큰 생성에 활용한다.
        String NewrefreshToken = this.createToken(authentication);
        // 기존 리프레시 토큰과 새로 만든 액세스 토큰을 반환한다.

        return NewrefreshToken;
    }
}
