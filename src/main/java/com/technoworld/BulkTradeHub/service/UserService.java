package com.technoworld.BulkTradeHub.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.technoworld.BulkTradeHub.entity.User;
import com.technoworld.BulkTradeHub.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public void registerUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);
	}
	
	public User findUserById(int id) {
		Optional<User> user = userRepository.findById(id);
		
		if(user.isPresent()) {
			return user.get();
		}
		return null;
	}

	public void deleteUserById(int id) {
		userRepository.deleteById(id);
	}
}
