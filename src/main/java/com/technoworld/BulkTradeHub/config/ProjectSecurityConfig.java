package com.technoworld.BulkTradeHub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class ProjectSecurityConfig {
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/home/**").permitAll()
						.requestMatchers("/main/**").permitAll()
						.requestMatchers("/login").permitAll()
						.requestMatchers("/logout").permitAll()
						.requestMatchers("/registration").permitAll()
						.requestMatchers("/admin/**").hasRole("ADMIN")
						.requestMatchers("/retailShop/**").hasRole("RETAIL")
						.requestMatchers("/business/**").hasRole("BUSINESS")
						.requestMatchers("/allPermit/**").hasAnyRole("RETAIL", "SALESMAN", "BUSINESS")
						)
				.formLogin(form -> form.loginPage("/login").successHandler(customSuccessHandler())
						.failureUrl("/login?error=true").permitAll())
				.httpBasic(Customizer.withDefaults()).csrf(Customizer.withDefaults());
		return httpSecurity.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationSuccessHandler customSuccessHandler() {
		return new CustomLoginSuccessHandler();
	}
}
