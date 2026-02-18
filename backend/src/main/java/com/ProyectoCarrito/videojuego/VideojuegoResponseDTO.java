package com.ProyectoCarrito.videojuego;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideojuegoResponseDTO {

	// DATOS DEL PRODUCTO
	private Long id;
	private String nombre;
	private String descripcion;
	private BigDecimal precio;
	private Integer stock;
	private String imagenUrl;
	private String categoria;
	
	// DATOS DEL VIDEOJUEGO
	private String desarrollador;
	private LocalDate fechaLanzamiento;
	private Set<String> generos;
	private Set<String> plataformas;
}
