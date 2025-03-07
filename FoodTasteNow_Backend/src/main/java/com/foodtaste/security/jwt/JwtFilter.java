package com.foodtaste.security.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.foodtaste.security.jwt.Impl.CustomUserDetailsService;
import com.foodtaste.security.jwt.Impl.JwtServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

	public static String currentUser = "";

	private final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

	@Autowired
	private JwtServiceImpl jwtService;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		logger.debug("Processing request: {}", request);

		try {
			String authHeader = request.getHeader("Authorization");
			String jwtToken = null;
			String username = null;

			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				jwtToken = authHeader.substring(7);
				logger.debug("JWT token extracted from Authorization header.");

				username = jwtService.extractUsernameFromToken(jwtToken);
				currentUser = username;
				logger.debug("Username extracted from JWT token: {}", username);

				if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
					UserDetails userDetails = userDetailsService.loadUserByUsername(username);
					logger.debug("User details loaded for username: {}", username);

					if (jwtService.validateToken(jwtToken, userDetails)) {

						UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
								username, null, userDetails.getAuthorities());

						authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

						SecurityContextHolder.getContext().setAuthentication(authenticationToken);
						logger.debug("JWT token validated and authentication set.");
					}
				}

			}
		} catch (UsernameNotFoundException e) {
			logger.error("User not found: {}", e.getMessage(), e);
		} catch (Exception e) {
			logger.error("Error processing JWT token: {}", e.getMessage(), e);
		}
		filterChain.doFilter(request, response);

	}

}
