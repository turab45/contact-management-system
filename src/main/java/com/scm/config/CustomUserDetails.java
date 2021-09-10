package com.scm.config;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.scm.entities.UserEntity;

public class CustomUserDetails implements UserDetails{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private UserEntity userEntity;

	public CustomUserDetails(UserEntity userEntity2) {
		
		this.userEntity = userEntity2;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// user permissions
		
		SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(this.userEntity.getRoll());
		return List.of(grantedAuthority);
	}

	@Override
	public String getPassword() {
		// user password
		return userEntity.getPassword();
	}

	@Override
	public String getUsername() {
		// user email
		return userEntity.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
