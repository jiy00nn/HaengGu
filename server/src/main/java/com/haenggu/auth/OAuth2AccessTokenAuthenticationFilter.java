package com.haenggu.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haenggu.auth.jwt.Token;
import com.haenggu.auth.jwt.TokenProvider;
import com.haenggu.domain.enums.SocialType;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

@Component
public class OAuth2AccessTokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final String DEFAULT_OAUTH2_LOGIN_REQUEST_URL_PREFIX = "/api/oauth/login/";
    private static final String HTTP_METHOD = "GET";
    private static final String ACCESS_TOKEN_HEADER_NAME = "Authorization";

    private static final AntPathRequestMatcher DEFAULT_OAUTH2_LOGIN_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher(DEFAULT_OAUTH2_LOGIN_REQUEST_URL_PREFIX +"*", HTTP_METHOD); // "/api/oauth/login/*" 의 요청에, GET으로 온 요청에 매칭된다.

    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    public OAuth2AccessTokenAuthenticationFilter(AccessTokenAuthenticationProvider accessTokenAuthenticationProvider, TokenProvider tokenProvider, ObjectMapper objectMapper) {
        super(DEFAULT_OAUTH2_LOGIN_PATH_REQUEST_MATCHER);   // 위에서 설정한  "/api/oauth/login/*" 의 요청에, GET으로 온 요청을 처리하기 위해 설정한다.
        this.tokenProvider = tokenProvider;
        this.objectMapper = objectMapper;
        this.setAuthenticationManager(new ProviderManager(accessTokenAuthenticationProvider));

        this.setAuthenticationSuccessHandler((request, response, authentication) -> {
            OAuth2UserDetails oAuth2UserDetails = (OAuth2UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Token token = tokenProvider.createToken(oAuth2UserDetails.getId(), oAuth2UserDetails.getRoleType());
            writeTokenResponse(response, token);
        });
        this.setAuthenticationFailureHandler((request, response, exception) -> {
            // TODO 실패할 경우 response 정하기
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

    private void writeTokenResponse(HttpServletResponse response, Token token)
            throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        PrintWriter writer = response.getWriter();
        writer.println(objectMapper.writeValueAsString(token));
        writer.flush();
    }
}
