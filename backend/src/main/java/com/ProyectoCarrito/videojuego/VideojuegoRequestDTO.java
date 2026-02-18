package com.ProyectoCarrito.videojuego;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VideojuegoRequestDTO {
	
	// DATOS DEL PRODUCTO (GENERAL)

	@NotBlank(message = "El nombre es obligatorio")
	@Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
	private String nombre;

	@Size(max = 250, message = "La descripción no puede exceder los 250 caracteres")
	private String descripcion;

	@NotNull(message = "El precio es obligatorio")
	@DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
	@Digits(integer = 8, fraction = 2, message = "Formato de precio inválido")
	private BigDecimal precio;

	@NotNull(message = "El stock es obligatorio")
	@Min(value = 0, message = "El stock no puede ser negativo")
	private Integer stock;

	@Size(max = 250, message = "La descripción no puede exceder los 250 caracteres")
	private String imagenUrl;

	@NotNull(message = "La categoría es obligatoria")
	private Long categoriaId;
	
	// DATOS DEL VIDEOJUEGO (ESPECIFICO)

	@Size(max = 100, message = "El nombre del desarrollador no puede exceder los 100 caracteres")
	private String desarrollador;

	@NotNull(message = "La fecha de lanzamiento es obligatoria")
	private LocalDate fechaLanzamiento;
	
	@NotEmpty(message = "Debe seleccionar al menos un género")
	private Set<Long> generosIds;
	
	@NotEmpty(message = "Debe seleccionar al menos una plataforma")
	private Set<Long> plataformasIds;
}
