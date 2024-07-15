package com.weather.forecast.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.weather.forecast.configuration.UserInfoDetails;
import com.weather.forecast.entity.UserInfo;
import com.weather.forecast.repository.UserInfoRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserInfoRepository userInfoRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// Here, you should load the user from your database or other source
		// For demonstration, we are using a hardcoded user
		Optional<UserInfo> userinfo = userInfoRepository.findByName(username);
		return userinfo.map(UserInfoDetails::new).orElseThrow(() -> new UsernameNotFoundException(username));
	}

}