package com.foodtaste.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Base64;

@Component
public class JWTUtil {

	// Secure Secret Key (Base64 encoded for better security)
	private static final String SECRET = "VGhpcy1Jcy1Bbi1FY29kZWQtU2VjcmV0S2V5LVRoYXQtaXMtU2FmZQ=="; // Replace with a
																										// generated key
	private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET));

	// Token expiration time (1 hour)
	private static final long EXPIRATION_TIME = 1000 * 60 * 60;

	/**
	 * Generates a JWT token for the given UserDetails. Stores the username and
	 * roles in the token claims.
	 */
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();

		// Add roles as a claim
		List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());
		claims.put("roles", roles);

		return createToken(claims, userDetails.getUsername());
	}
	

	/**
	 * Creates a JWT token with claims, subject (username), issued time, expiration,
	 * and signing key.
	 */
	private String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().claims(claims).
				subject(subject).
				issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)).
				signWith(SECRET_KEY) // Secure signing
				.compact();
	}

	/**
	 * Extracts the username from a given JWT token.
	 */
	public String extractUsername(String token) {
		Claims claims = extractAllClaims(token);
        return claims.getSubject();
	}

	/**
	 * Extracts the expiration date from the token.
	 */
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	/**
	 * Generic method to extract any claim using a function.
	 */
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	/**
	 * Extracts all claims from a token safely.
	 */
	private Claims extractAllClaims(String token) {
		try {
			return Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
		} catch (ExpiredJwtException e) {
			throw new RuntimeException("JWT Token has expired", e);
		} catch (JwtException e) {
			throw new RuntimeException("Invalid JWT Token", e);
		}
	}

	/**
	 * Checks if the token is expired.
	 */
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	/**
	 * Validates the token by checking the username and expiration.
	 */
	public boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}
