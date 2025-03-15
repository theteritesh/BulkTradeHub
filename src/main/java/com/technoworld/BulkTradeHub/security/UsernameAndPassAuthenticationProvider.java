package com.technoworld.BulkTradeHub.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.technoworld.BulkTradeHub.entity.User;
import com.technoworld.BulkTradeHub.repository.UserRepository;

@Component
public class UsernameAndPassAuthenticationProvider implements AuthenticationProvider{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String email=authentication.getName();
		String password=authentication.getCredentials().toString();
		User user=userRepository.readByEmail(email);
		if(user !=null && user.getId()>0 && passwordEncoder.matches(password, user.getPassword())) {
			return new UsernamePasswordAuthenticationToken(user.getName(), password,getGrantedAuthorities(user.getRoles()));
		}else {
			throw new BadCredentialsException("Invalid credentials");
		}
	}
	
	private List<GrantedAuthority> getGrantedAuthorities(String roles){
		List<GrantedAuthority> grantedAuthorities=new ArrayList<>();
		grantedAuthorities.add(new SimpleGrantedAuthority(roles));
		return grantedAuthorities;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
