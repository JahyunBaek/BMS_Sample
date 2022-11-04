package com.szs.jobis.security;

import com.szs.jobis.Config.Provider.RefreshTokenProvider;
import com.szs.jobis.Config.Provider.TokenProvider;
import com.szs.jobis.Entity.TokenEntity;
import com.szs.jobis.util.TokenStatus;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static final String REFRESH_HEADER = "AuthorizationRefresh";

    private final TokenProvider tokenProvider;

    private final RefreshTokenProvider refreshProvider;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        //HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = resolveToken(request,AUTHORIZATION_HEADER);
        String requestURI = request.getRequestURI();

        // 유효성 검증
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt) == TokenStatus.Access) {
            // 토큰에서 유저네임, 권한을 뽑아 스프링 시큐리티 유저를 만들어 Authentication 반환
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            // 해당 스프링 시큐리티 유저를 시큐리티 건텍스트에 저장, 즉 디비를 거치지 않음
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
        }else if(StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt) == TokenStatus.Expire){
            String refresh = resolveToken(request, REFRESH_HEADER);
            
            if(StringUtils.hasText(refresh) && refreshProvider.validateToken(refresh) == TokenStatus.Access){
                String newRefresh = refreshProvider.reissueRefreshToken(refresh);
                if(StringUtils.hasText(newRefresh)){
                    response.setHeader(REFRESH_HEADER, "Bearer-"+newRefresh);

                    // access token 생성
                    Authentication authentication = tokenProvider.getAuthentication(refresh);
                    response.setHeader(AUTHORIZATION_HEADER, "Bearer-"+tokenProvider.createToken(authentication));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.debug("JWT 토큰 재발급에 성공하였습니다.");
                }
            }else{
                logger.debug("유효한 JWT 리프레시 토큰이 없습니다, uri: {}", requestURI);
            }
        }
         else {
            logger.debug("유효한 JWT 액세스 토큰이 없습니다, uri: {}", requestURI);
        }

        filterChain.doFilter(request, response);
    }

    // 헤더에서 토큰 정보를 꺼내온다.
    private String resolveToken(HttpServletRequest request, String header) {
        String bearerToken = request.getHeader(header);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}