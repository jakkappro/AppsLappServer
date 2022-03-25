package org.appslapp.AppsLappServer.business.security.entity;

import org.appslapp.AppsLappServer.business.pojo.users.entity.Entity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class EntityDetails<T extends Entity> implements UserDetails {
    protected  T user;

    protected EntityDetails(T user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(user.getGrantedAuthority());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
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
        return user.isEnabled();
    }

    public long getId() {
        return user.getId();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getLastName() {
        return user.getLastName();
    }

    public String getFirstName() {
        return user.getFirstName();
    }
}
