package com.technoworld.BulkTradeHub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ProjectSecurityConfig {
	@Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(auth -> auth
                .requestMatchers("/home","/").permitAll()
                .requestMatchers("/admin/**").permitAll()            
                .requestMatchers("/main/**").permitAll()
                .requestMatchers("/login").permitAll()
                .requestMatchers("/logout").permitAll()
                .requestMatchers("/registration").permitAll()
                .requestMatchers("/retailshop/**").permitAll()
                .requestMatchers("/dashboard/**").hasRole("RETAIL")
                .requestMatchers("/retail/**").hasRole("RETAIL")
                .requestMatchers("/products/**").hasRole("RETAIL")
                .requestMatchers("/retailShop/**").hasRole("RETAIL")
            )
            
        	.formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/home", true) 
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .httpBasic(Customizer.withDefaults())
            .csrf(Customizer.withDefaults());
        return httpSecurity.build();
    }
	
	
    @Bean
    PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder(); 
    }
}
