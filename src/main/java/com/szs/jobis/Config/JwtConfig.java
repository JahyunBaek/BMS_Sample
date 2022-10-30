package com.szs.jobis.Config;

import com.szs.jobis.Config.Provider.RefreshTokenProvider;
import com.szs.jobis.Config.Provider.TokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    @Value("${jwt.secret}")
    private String accessTokenSecret;
    @Value("${jwt.refresh-token-secret}")
    private String refreshTokenSecret;
    @Value("${jwt.access-token-validity-in-seconds}")
    private Long accessTokenValidityInSeconds;
    @Value("${jwt.refresh-token-validity-in-seconds}")
    private Long refreshTokenValidityInSeconds;

    // 액세스 토큰 발급용, 리프레시 토큰 발급용은 각각 별도의 키와 유효기간을 갖는다.
    @Bean(name = "tokenProvider")
    public TokenProvider tokenProvider() {
        return new TokenProvider(accessTokenSecret, accessTokenValidityInSeconds);
    }

    @Bean(name = "refreshTokenProvider")
    public RefreshTokenProvider refreshTokenProvider() {
        return new RefreshTokenProvider(refreshTokenSecret, refreshTokenValidityInSeconds);
    }
}