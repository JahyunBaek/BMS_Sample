package com.szs.jobis.Config;

import com.szs.jobis.Config.Provider.JwtSecurityConfig;
import com.szs.jobis.Config.Provider.TokenProvider;
import com.szs.jobis.security.handler.JwtAccessDeniedHandler;
import com.szs.jobis.security.handler.JwtAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final TokenProvider tokenProvider;
    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    // 생성자 통해 스프링 빈 주입받는다.
    public SecurityConfig(
            TokenProvider tokenProvider,
            CorsFilter corsFilter,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAccessDeniedHandler jwtAccessDeniedHandler
    ) {
        this.tokenProvider = tokenProvider;
        this.corsFilter = corsFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(
                        "/h2/**"
                        ,"/h2-console/**"
                        ,"/error"
                        ,"/swagger-ui.html"
                );
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // token을 사용하는 방식이기 때문에 csrf를 disable합니다.
                .csrf()
                .ignoringAntMatchers("/h2-console/**", "/swagger-ui.html")
                .disable()

                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 우리가 만든 클래스로 인증 실패 핸들링
                .accessDeniedHandler(jwtAccessDeniedHandler) // 커스텀 인가 실패 핸들링

                // enable h2-console // embedded h2를 위한 설정
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                // 세션을 사용하지 않기 때문에 STATELESS로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/h2-console/**", "/szs/signup", "/swagger-ui.html").permitAll()

                // api 경로
                .and()
                .authorizeRequests()
                .antMatchers("/szs/signup").permitAll() // 회원 가입
                .antMatchers("/szs/login").permitAll() // 로그인
                .antMatchers("/szs/refresh").permitAll() // 토큰 Refresh
                .anyRequest().authenticated() // 나머지 경로는 jwt 인증 해야함

                .and()
                .apply(new JwtSecurityConfig(tokenProvider)); // JwtSecurityConfig 적용
    }
}