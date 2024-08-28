package org.example.config;

import java.io.IOException;
import java.util.Arrays;

import org.example.config.jwt.TokenProvider;
import org.example.config.oauth.OAuth2SuccessHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)  throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            filterChain.doFilter(request, response);
            return;
        }
/*        Arrays.stream(cookies)
              .filter(cookie -> cookie.getName().equals(OAuth2SuccessHandler.ACCESS_TOKEN_COOKIE_NAME))
              .findFirst()
              .ifPresent(cookie -> {
                  String token = cookie.getValue();
                  if (tokenProvider.validToken(token)) {
                      Authentication authentication = tokenProvider.getAuthentication(token);
                      SecurityContextHolder.getContext().setAuthentication(authentication);
                  }
              });*/
        Arrays.stream(cookies)
            .filter(cookie -> cookie.getName().equals(OAuth2SuccessHandler.ACCESS_TOKEN_COOKIE_NAME))
            .findFirst()
            .ifPresent(cookie -> {
                try {
                    String token = cookie.getValue();
                    if (tokenProvider.validToken(token)) {
                        Authentication authentication = tokenProvider.getAuthentication(token);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } catch (Exception e) {
                    log.error("Error setting authentication from token", e);
                }
            });

        filterChain.doFilter(request, response);
    }

    /**
     * Auth 헤더가 아닌 Cookie에서 Token을 전달 받으므로, 현재는 사용하지 않습니다.
     */
    private String getAccessToken(String authorizationHeader) {
        String TOKEN_PREFIX = "Bearer ";
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            return authorizationHeader.substring(TOKEN_PREFIX.length());
        }

        return null;
    }
}
