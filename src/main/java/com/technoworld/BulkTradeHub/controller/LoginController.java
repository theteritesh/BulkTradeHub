package com.technoworld.BulkTradeHub.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.technoworld.BulkTradeHub.entity.User;
import com.technoworld.BulkTradeHub.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class LoginController {
	
	private final UserService userService;
	
	public LoginController(UserService userService) {
		this.userService=userService;
	}		
	
	@RequestMapping(value = "/login",method = {RequestMethod.GET,RequestMethod.POST})
	public String displayLoginPage(@RequestParam(value = "error",required = false) String error,
								@RequestParam(value = "logout",required = false) String logout,
								@RequestParam(value = "register",required = false) String register,
								Model model) {
		String errorMessage=null;
		if(error != null) {
			errorMessage="Username or Password is incorrect!! ";
		}
		if(logout != null) {
			errorMessage="You have been successfully loged out!!";
		}
		
		if(register != null) {
			errorMessage="You have been successfully registered!!";
		}
		
		model.addAttribute("errorMessage",errorMessage);
		return"login";
	}
	
	@GetMapping("/logout")
	public String logoutPage(HttpServletRequest request,HttpServletResponse response) {
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		if(authentication != null) {
			new SecurityContextLogoutHandler().logout(request, response, authentication);
		}
		return "redirect:/login?logout=true";
	}
	
	@GetMapping("/registration")
	public String registrationPage(Model model) {
		model.addAttribute("user", new User());
		return "registration";
	}
	
	@PostMapping("/registration")
	public String registerUser(@RequestParam("name") String name ,
							@RequestParam("email") String email ,
							@RequestParam("password") String password,
							@RequestParam("roles") String roles) {
		User user=new User();
		user.setName(name);
		user.setEmail(email);
		user.setPassword(password);
		user.setRoles(roles);
		userService.registerUser(user);
		return "redirect:/login?register=true";
	}
}
