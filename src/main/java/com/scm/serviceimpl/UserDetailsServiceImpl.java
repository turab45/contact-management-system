package com.scm.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.scm.config.CustomUserDetails;
import com.scm.dao.UserRepository;
import com.scm.entities.UserEntity;

public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserEntity userEntity = userRepository.findByEmail(username);
		
		if (userEntity == null) {
			throw new UsernameNotFoundException("Could not find user");
		}
		
		CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);
		return customUserDetails;
	}

}
