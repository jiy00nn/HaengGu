package com.haenggu.auth;

import com.haenggu.domain.enums.SocialType;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Component
public class OAuth2AccessTokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final String DEFAULT_OAUTH2_LOGIN_REQUEST_URL_PREFIX = "/api/oauth/login/";
    private static final String HTTP_METHOD = "GET";
    private static final String ACCESS_TOKEN_HEADER_NAME = "Authorization";

    private static final AntPathRequestMatcher DEFAULT_OAUTH2_LOGIN_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher(DEFAULT_OAUTH2_LOGIN_REQUEST_URL_PREFIX +"*", HTTP_METHOD); // "/api/oauth/login/*" 의 요청에, GET으로 온 요청에 매칭된다.

    public OAuth2AccessTokenAuthenticationFilter(AccessTokenAuthenticationProvider accessTokenAuthenticationProvider) {
        super(DEFAULT_OAUTH2_LOGIN_PATH_REQUEST_MATCHER);   // 위에서 설정한  "/api/oauth/login/*" 의 요청에, GET으로 온 요청을 처리하기 위해 설정한다.
        this.setAuthenticationManager(new ProviderManager(accessTokenAuthenticationProvider));
        this.setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                OAuth2UserDetails oAuth2UserDetails = (OAuth2UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                response.sendRedirect("/api/oauth/success/"+oAuth2UserDetails.getId());
            }
        });
        this.setAuthenticationFailureHandler(new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                // TODO 실패할 경우 response 정하기
            }
        });
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        SocialType socialType = extractSocialType(request);
        String accessToken = request.getHeader(ACCESS_TOKEN_HEADER_NAME); //헤더의 AccessToken에 해당하는 값을 가져온다.

        return this.getAuthenticationManager().authenticate(new AccessTokenSocialTypeToken(accessToken, socialType));
    }


    private SocialType extractSocialType(HttpServletRequest request) {
        return Arrays.stream(SocialType.values())
                .filter(socialType -> socialType.getSocialName()
                                .equals(request.getRequestURI().substring(DEFAULT_OAUTH2_LOGIN_REQUEST_URL_PREFIX.length())))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 URL 주소입니다"));
    }
}
