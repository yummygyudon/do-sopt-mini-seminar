package org.sopt.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.entity.Authority;
import org.sopt.entity.MiminarUser;
import org.sopt.entity.base.CustomUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class CustomInternalAuthUser implements UserDetails, CustomUser {
    // They simply store user information which is later encapsulated into Authentication objects.

    private final MiminarUser customUser;
    private final List<Authority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getRole().name()))
                .toList();
    }

    @Override
    public String getPassword() {
        return this.customUser.getPassword();
    }

    @Override
    public String getUsername() {
        return this.customUser.getName();
    }

    @Override
    public String getEmail() {
        return this.customUser.getEmail();
    }
    @Override
    public String getPlatform() {
        return this.customUser.getPlatform().name();
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
