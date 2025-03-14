package com.technoworld.BulkTradeHub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ProjectSecurityConfig {
	@Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(auth -> auth
                .requestMatchers("/home","/").permitAll()
                .requestMatchers("/main/**").permitAll()
                .requestMatchers("/login").permitAll()
                .requestMatchers("/retailshop/**").permitAll()
                .requestMatchers("/dashboard/**").authenticated()
                .requestMatchers("/products/**").authenticated()
            )
            
        	.formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/home", true) 
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .permitAll()
            )
            .httpBasic(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable());
        return httpSecurity.build();
    }
	
	@Bean
	InMemoryUserDetailsManager userDetailsService() {
		UserDetails admin= User.builder()
				.username("admin")
				.password(passwordEncoder().encode("1234"))
				.roles("ADMIN","RETAIL")
				.build();
		
		UserDetails retail= User.builder()
				.username("retail")
				.password(passwordEncoder().encode("1234"))
				.roles("RETAIL")
				.build();
		
		return new InMemoryUserDetailsManager(admin,retail);
	}

    @Bean
    PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder(); // Secure password hashing
    }
}
