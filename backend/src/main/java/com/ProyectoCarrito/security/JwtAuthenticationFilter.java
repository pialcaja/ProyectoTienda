package com.ProyectoCarrito.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

// INTERCEPTA CADA PETICION Y VALIDA EL TOKEN JWT

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	private final JwtTokenProvider tokenProvider;
	private final CustomUserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(
			HttpServletRequest request, 
			HttpServletResponse response, 
			FilterChain filterChain)
			throws ServletException, IOException {
		
		try {
			// OBTIENE EL TOKEN DEL HEADER AUTHORIZATION
			String token = obtenerTokenDePeticion(request);
			
			// VALIDA EL TOKEN
			if (StringUtils.hasText(token) && 
					tokenProvider.validarToken(token)) {
				// OBTENER EL EMAIL DEL TOKEN
				String email = tokenProvider.obtenerEmailDeToken(token);
				
				// CARGA AL USUARIO DESDE LA BD INCLUYENDO EL PREFIJO "ROLE_"
				UserDetails userDetails = userDetailsService
						.loadUserByUsername(email);
				
				// CREA EL OBJETO DE AUTENTICACION
				UsernamePasswordAuthenticationToken authentication = 
						new UsernamePasswordAuthenticationToken(
								userDetails,
								null,
								userDetails.getAuthorities());
				
				authentication.setDetails(
						new WebAuthenticationDetailsSource()
						.buildDetails(request));
				
				// SETEA AUTENTICACION EN EL CONTEXTO DE SEGURIDAD
				SecurityContextHolder.getContext()
				.setAuthentication(authentication);
			}
		} catch (Exception e) {
			logger.error(
					"No se pudo establecer la autenticación del usuario", e);
		}
		
		// CONTINUA CON LA CADEDENA DE FILTROS
		filterChain.doFilter(request, response);
	}

	// EXTRAE EL TOKEN JWT DEL HEADER AUTHORIZATION
	private String obtenerTokenDePeticion(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		
		if (StringUtils.hasText(bearerToken) && 
				bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		
		return null;
	}
}
