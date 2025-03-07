package com.foodtaste.security.jwt.Impl;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.foodtaste.security.jwt.JWTConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl {

	private final JWTConfig jwtConfig;
	private final UserDetailsService userDetailsService;
	private static final Logger logger = LoggerFactory.getLogger(JwtServiceImpl.class);

	public JwtServiceImpl(JWTConfig jwtConfig, UserDetailsService userDetailsService) {
		this.jwtConfig = jwtConfig;
		this.userDetailsService = userDetailsService;
	}

	public String generateToken(String username) {
		logger.info("Generating JWT token for user: {}", username);
		if (username == null || username.trim().isEmpty()) {
			logger.error("Username cannot be null or empty");
			throw new IllegalArgumentException("Username cannot be null or empty");
		}

		try {
			Date expirationDate = calculateExpirationDate();
			Collection<? extends GrantedAuthority> authorities = fetchUserAuthorities(username);
			Map<String, Object> claims = buildClaims(username, authorities);
			return buildJwtToken(claims, expirationDate);
		} catch (Exception e) {
			logger.error("Failed to generate JWT token for user: {}", username, e);
			throw new RuntimeException("Failed to generate JWT token", e);
		}
	}

	/*
	 * Calculate the expiration date based on the configuration.
	 */
	private Date calculateExpirationDate() {
		String expirationString = jwtConfig.getExpiration();
		try {
			long expirationSeconds = Long.parseLong(expirationString);
			Instant expirationInstant = Instant.now().plusSeconds(expirationSeconds);
			return Date.from(expirationInstant);
		} catch (NumberFormatException ex) {
			logger.error("Invalid expiration time configuration: {}", expirationString, ex);
			throw new IllegalArgumentException("Invalid expiration time configuration", ex);
		}
	}

	/*
	 * Fetch the user authorities from UserDetailsService.
	 */
	private Collection<? extends GrantedAuthority> fetchUserAuthorities(String username) {
		try {
			return userDetailsService.loadUserByUsername(username).getAuthorities();
		} catch (UsernameNotFoundException e) {
			logger.error("User not found: {}", username, e);
			throw new RuntimeException("User not found: " + username, e);
		}
	}

	/*
	 * Build the JWT claims including subject and authorities
	 */
	private Map<String, Object> buildClaims(String username, Collection<? extends GrantedAuthority> authorities) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("sub", username);
		claims.put("authorities", authorities);
		return claims;
	}

	/*
	 * Build and sign the JWT token
	 */
	private String buildJwtToken(Map<String, Object> claims, Date expirationDate) {
		return Jwts.builder().setClaims(claims).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(expirationDate).signWith(getKey()).compact();
	}

	/*
	 * Retrieve the secret key used for signing the token
	 */
	private SecretKey getKey() {
		try {
			byte[] keyBytes = Decoders.BASE64.decode(jwtConfig.getSecretKey());
			if (keyBytes.length < 32) {
				throw new IllegalArgumentException("Secret key must be at least 256 bits (32 bytes)");
			}
			return Keys.hmacShaKeyFor(keyBytes);
		} catch (Exception e) {
			logger.error("Failed to decode or create secret key: {}", e.getMessage(), e);
			throw new IllegalArgumentException("Invalid secret key", e);
		}
	}

	/*
	 * Extract the username (subject) from the JWT token
	 */
	public String extractUsernameFromToken(String token) {
		logger.info("Extracting username from token");
		return getClaimsFromToken(token, Claims::getSubject);
	}

	/*
	 * Extract a specific claim from the token using a resolver function
	 */
	private <T> T getClaimsFromToken(String token, Function<Claims, T> claimResolver) {
		final Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}

	/*
	 * Extract all claims from the JWT token
	 */
	private Claims extractAllClaims(String token) {
		try {
			return Jwts.parser().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage(), e);
			throw new SecurityException("JWT token is expired", e);
		} catch (MalformedJwtException e) {
			logger.error("JWT token is malformed: {}", e.getMessage(), e);
			throw new SecurityException("JWT token is malformed", e);
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage(), e);
			throw new SecurityException("JWT token is unsupported", e);
		} catch (IllegalArgumentException e) {
			logger.error("JWT token is invalid: {}", e.getMessage(), e);
			throw new SecurityException("JWT token is invalid", e);
		}
	}

	/*
	 * Validate the token by comparing username and checking expiration
	 */
	public boolean validateToken(String token, UserDetails userDetails) {
		logger.info("Validating JWT token for user: {}", userDetails.getUsername());
		try {
			final String username = extractUsernameFromToken(token);
			return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
		} catch (Exception e) {
			logger.error("Failed to validate JWT token: {}", e.getMessage(), e);
			return false;
		}
	}

	/*
	 * Check if the token is expired
	 */
	private boolean isTokenExpired(String token) {
		return tokenExpireDate(token).before(new Date());
	}

	/*
	 * Extract the expiration date from the token
	 */
	private Date tokenExpireDate(String token) {
		return getClaimsFromToken(token, Claims::getExpiration);
	}

}
