package com.haenggu.auth;

import com.haenggu.domain.entity.Users;
import com.haenggu.domain.enums.RoleType;
import com.haenggu.repository.UserRepository;
import com.haenggu.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AccessTokenAuthenticationProvider implements AuthenticationProvider {//AuthenticationProvider을 구현받아 authenticate와 supports를 구현해야 한다.

    private final UserService userService;  //restTemplate를 통해서 AccessToken을 가지고 회원의 정보를 가져오는 역할을 한다.
    private final UserRepository userRepository;//받아온 정보를 통해 DB에서 회원을 조회하는 역할을 한다.

    @SneakyThrows
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {//ProviderManager가 호출한다. 인증을 처리한다.

        OAuth2UserDetails oAuth2User = userService.getOAuth2UserDetails((AccessTokenSocialTypeToken) authentication);

        Users user = saveOrGet(oAuth2User);//받아온 식별자 값과 social로그인 방식을 통해 회원을 DB에서 조회 후 없다면 새로 등록해주고, 있다면 그대로 반환한다.
        oAuth2User.setRoles(user.getRoleType().name());//우리의 Role의 name은 ADMIN, USER, GUEST로 ROLE_을 붙여주는 과정이 필요하다. setRolse가 담당한다.
        oAuth2User.setId(user.getUserId());

        return AccessTokenSocialTypeToken.builder().principal(oAuth2User).authorities(oAuth2User.getAuthorities()).build();
    }

    private Users saveOrGet(OAuth2UserDetails oAuth2User) {
        return userRepository.findBySocialTypeAndPrincipal(oAuth2User.getSocialType(), oAuth2User.getPrincipal())
                .orElseGet(() -> userRepository.save(Users.builder()
                        .principal(oAuth2User.getPrincipal())
                        .socialType(oAuth2User.getSocialType())
                        .roleType(RoleType.GUEST)
                        .build()));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AccessTokenSocialTypeToken.class.isAssignableFrom(authentication); //AccessTokenSocialTypeToken타입의  authentication 객체이면 해당 Provider가 처리한다.
    }
}
