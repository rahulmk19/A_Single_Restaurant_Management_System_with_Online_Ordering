package com.foodtaste.config;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.foodtaste.security.jwt.JwtFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private JwtFilter jwtFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				// Set session management to stateless (no HTTP session)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				
				// Disable CSRF as we are not using cookies (JWT based)
				.csrf(AbstractHttpConfigurer::disable)
				
				// Configure CORS to allow your frontend application to communicate with this API	
				.cors(cors -> cors.configurationSource(request -> {
					CorsConfiguration cfg = new CorsConfiguration();
					cfg.setAllowedOriginPatterns(Collections.singletonList("*")); // Allow all origins – adjust for production!
					cfg.setAllowedMethods(Collections.singletonList("*")); // Allow all HTTP methods
					cfg.setAllowedHeaders(Collections.singletonList("*")); // Allow all headers
					cfg.setAllowCredentials(true); // Allow credentials (cookies, authorization headers, etc.)
					cfg.setExposedHeaders(List.of("Authorization")); // Expose the Authorization header
					return cfg;
				}))
				
				
				.authorizeHttpRequests(authorize -> authorize.requestMatchers("/auth/**", "/index").permitAll()
						.anyRequest().authenticated())
				
				// Add the custom JWT filter before the UsernamePasswordAuthenticationFilter in the filter chain
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	// Expose the AuthenticationManager for use in your authentication controller
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
