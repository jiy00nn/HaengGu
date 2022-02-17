package com.haenggu.auth;

import com.haenggu.domain.enums.RoleType;
import com.haenggu.domain.enums.SocialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.*;

@AllArgsConstructor
@Builder
public class OAuth2UserDetails implements UserDetails {

    private static final long serialVersionUID = 1L;
    private SocialType socialType;
    private UUID id;
    private RoleType roleType;
    private String principal;
    private String username;
    private Set<GrantedAuthority> authorities;

    public SocialType getSocialType() {
        return socialType;
    }

    public UUID getId() { return id; }

    public RoleType getRoleType() { return roleType; }

    public String getPrincipal() {
        return principal;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() { return null; }

    @Override
    public String getUsername() { return username; }

    public void setRoles(String... roles) {
        List<GrantedAuthority> authorities = new ArrayList<>(roles.length);

        for (String role : roles) {
            Assert.isTrue(!role.startsWith("ROLE_"),
                    () -> role + " cannot start with ROLE_ (it is automatically added)");
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            this.roleType = Arrays.stream(RoleType.values())
                    .filter(type ->
                        type.getGrantedAuthority().equals("ROLE_" + role))
                    .findFirst().get();
        }
        this.authorities = Set.copyOf(authorities);
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
