package com.technoworld.BulkTradeHub.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.technoworld.BulkTradeHub.entity.User;
import com.technoworld.BulkTradeHub.entity.VerificationToken;
import com.technoworld.BulkTradeHub.repository.UserRepository;
import com.technoworld.BulkTradeHub.repository.VerificationTokenRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
    @Autowired
    private VerificationTokenRepository tokenRepo;
    

    @Autowired
    private EmailService emailService;
	
	public void registerUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setVerified(false);
		userRepository.save(user);
		 String token = UUID.randomUUID().toString();
	        VerificationToken vToken = new VerificationToken(token, user);
	        tokenRepo.save(vToken);
	        emailService.sendVerificationEmail(user, token);
	}
	
	 public String verifyToken(String token) {
	        VerificationToken vToken = tokenRepo.findByToken(token);
	        if (vToken == null || vToken.getExpiryDate().isBefore(LocalDateTime.now())) {
	            return "Invalid or expired token!";
	        }

	        User user = vToken.getUser();
	        user.setVerified(true);
	        userRepository.save(user);
	        tokenRepo.delete(vToken);

	        return "Email verified successfully!";
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
	
	public User saveEditUser(User user) {
		return userRepository.save(user);
	}
}
