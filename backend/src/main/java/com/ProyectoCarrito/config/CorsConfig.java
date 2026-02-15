package com.ProyectoCarrito.config;

import org.springframework.web.filter.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

	// PERMITE QUE EL FRONT PUEDA HACER PETICIONES AL BACK
	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		
		// PERMITIR CREDENCIALES
		config.setAllowCredentials(true);
		
		// ORIGENES PERMITIDOS
		config.setAllowedOrigins(Arrays.asList(
				"http://localhost:4200"
		));
		
		// HEADER PERMITIDOS
		config.setAllowedHeaders(Arrays.asList(
				"Origin",
				"Content-Type",
				"Accept",
				"Authorization",
				"Access-Control-Request-Method",
				"Access-Control-Request-Headers"
		));
		
		// METODOS HTTP PERMITIDOS
		config.setAllowedMethods(Arrays.asList(
				"GET",
				"POST",
				"PUT",
				"DELETE",
				"BATCH",
				"OPTIONS"
		));
		
		// HEADERS EXPUESTOS AL CLIENTE
		config.setExposedHeaders(List.of("Authorization"));
		
		// APLICAR CONFIGURACION A TODOS LOS ENDPOINTS
		source.registerCorsConfiguration("/api/**", config);
		
		return new CorsFilter(source);
	}
}
