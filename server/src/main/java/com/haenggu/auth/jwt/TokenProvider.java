package com.haenggu.auth.jwt;

import com.haenggu.domain.enums.RoleType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class TokenProvider implements InitializingBean {
    private static final String ROLE_KEY = "ROLE";

    private final String secret;
    private final long tokenValidityInMilliseconds;
    private final long tokenRefreshValidityInMilliseconds;

    private Key key;

    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInMilliseconds,
            @Value("${jwt.token-refresh-validity-in-seconds}") long tokenRefreshValidityInMilliseconds) {
        this.secret = secret;
        this.tokenValidityInMilliseconds = tokenValidityInMilliseconds;
        this.tokenRefreshValidityInMilliseconds = tokenRefreshValidityInMilliseconds;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public Token createToken(UUID id, RoleType roleType) {

        Claims claims = Jwts.claims().setSubject(id.toString());
        claims.put(ROLE_KEY, roleType.getGrantedAuthority());

        Date now = new Date();
        Date validity = new Date(now.getTime() + this.tokenValidityInMilliseconds);
        Date refreshValidity = new Date(now.getTime() + this.tokenRefreshValidityInMilliseconds);

        return new Token(
                Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(new Date())
                        .setExpiration(validity)
                        .signWith(key, SignatureAlgorithm.HS512)
                        .compact(),
                Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(new Date())
                        .setExpiration(refreshValidity)
                        .signWith(key, SignatureAlgorithm.HS512)
                        .compact()
        );
    }

    // token 값을 통하여 Authentication 객체를 리턴하는 메소드
    public UUID getId(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return UUID.fromString(claims.getSubject());
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(ROLE_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(claims.getSubject(), token, authorities);
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            System.out.println("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            System.out.println("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            System.out.println("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            System.out.println("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
}
