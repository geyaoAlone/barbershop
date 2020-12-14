package com.geyao.barbershop.security;

import com.geyao.barbershop.user.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class JwtUserDetails implements UserDetails {

    private List<String> authorities;
    private User user;

    public JwtUserDetails(List<String> authorities, User user) {
        this.authorities = authorities;
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> simpleGrantedAuthorities =
                authorities.stream().map(e -> new SimpleGrantedAuthority(e)).collect(Collectors.toList());
        return simpleGrantedAuthorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return user.getMobile();
    }

    @Override
    public boolean isAccountNonExpired() {
        return "0".equals(user.getStatus()) ? false : true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return "0".equals(user.getStatus()) ? false : true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return "0".equals(user.getStatus()) ? false : true;
    }
}