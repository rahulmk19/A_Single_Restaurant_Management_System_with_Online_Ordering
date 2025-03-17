package com.foodtaste.security.jwt;

import java.util.Arrays;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.foodtaste.constant.SecurityConstant;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfiguration.class);

    private final JwtFilter jwtFilter;
    private final UserDetailsService userDetailsService;

    public WebSecurityConfiguration(JwtFilter jwtFilter, UserDetailsService userDetailsService) {
        this.jwtFilter = jwtFilter;
        this.userDetailsService = userDetailsService;
    }
    

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	logger.info("Configuring security filter chain...");
        http
            .csrf(csrf -> csrf.disable())
            .cors().and()
            .authorizeHttpRequests(authorize -> authorize
    		// Public endpoints
            		.requestMatchers(
            			    SecurityConstant.public_Auth, 
            			    SecurityConstant.public_MenuItem,
            			    "/swagger-ui/**",          // Allow Swagger UI
            			    "/v3/api-docs/**",         // Allow OpenAPI JSON docs
            			    "/swagger-ui.html",        // Alternative Swagger UI path
            			    "/swagger-resources/**",   // Swagger resources
            			    "/webjars/**"              // WebJars for Swagger UI
            			)
            			.permitAll()
                // Admin endpoints
                .requestMatchers(SecurityConstant.admin_MenuItem,SecurityConstant.admin_OrderDetails, SecurityConstant.admin_RoleDetails,SecurityConstant.admin_UserDetails)
                    .hasRole("ADMIN")
                // Endpoints common to both Admin and User roles
                .requestMatchers(SecurityConstant.common_OrderDetails,SecurityConstant.common_UserDetails)
                    .hasAnyRole("ADMIN", "USER")
                // User endpoints
                .requestMatchers(SecurityConstant.user_Cart,SecurityConstant.user_OrderDetails,SecurityConstant.user_UserDetails)
                    .hasRole("USER")
                
                // All other endpoints require authentication
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .httpBasic(Customizer.withDefaults());

        logger.info("Security filter chain configured successfully.");
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
    	logger.info("Configuring CORS...");
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        logger.info("CORS configured successfully.");
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
        daoProvider.setUserDetailsService(userDetailsService);
        daoProvider.setPasswordEncoder(passwordEncoder());
        return daoProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        try {
            logger.info("Creating AuthenticationManager bean...");
            return authenticationConfiguration.getAuthenticationManager();
        } catch (Exception e) {
            logger.error("Failed to create AuthenticationManager: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create AuthenticationManager", e);
        }
    }
}
