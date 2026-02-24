package com.ProyectoCarrito.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

// GENERA Y VALIDA TOKENS
@Component
public class JwtTokenProvider {

	@Value("${jwt.secret}")
	private String jwtSecret;
	
	@Value("${jwt.expiration}")
	private Long jwtExpiration;
	
	// GENERA TOKEN DESDE AUTHENTICATION
	public String generarToken(Authentication authentication) {
		
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtExpiration);
		
		// EXTRAE ROLES DEL USUARIO
		String roles = userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
		
		SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
		
		return Jwts.builder()
				.subject(userDetails.getUsername())
				.claim("roles", roles)
				.issuedAt(now)
				.expiration(expiryDate)
				.signWith(key)
				.compact();
	}
	
	// OBTIENE EMAIL DEL USUARIO DESDE EL JWT
	public String obtenerEmailDeToken(String token) {
		
		SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
		
		Claims claims = Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();
		
		return claims.getSubject();
	}
	
	// OBTIENE ROLES DEL TOKEN
	public String obtenerRolesDeToken(String token) {
		
		SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
		
		Claims claims = Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();
		
		return claims.get("roles", String.class);
	}
	
	// VALIDA TOKEN
	public boolean validarToken(String token) {
		
		try {
			
			SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
			
			Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token);
			
			return true;
					
		} catch (JwtException e) {
			System.err.println("Token JWT inválido: " + e.getMessage());
		} catch (IllegalArgumentException e) {
			System.err.println("JWT claims string está vacío");
		}
		
		return false;
	}
}
