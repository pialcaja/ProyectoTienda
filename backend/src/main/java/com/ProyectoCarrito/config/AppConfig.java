package com.ProyectoCarrito.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

	// BEAN PARA QUE SE PUEDA INYECTAR DONDE SE NECESITE
	// MODELMAPPER PARA CONVERSION AUTOMATICA ENTITY <-> DTO
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		
		// IGNORAR VALORES NULL AL MAPEAR
		modelMapper.getConfiguration()
		.setSkipNullEnabled(true)
		.setAmbiguityIgnored(true);
		
		return modelMapper;
	}
}
