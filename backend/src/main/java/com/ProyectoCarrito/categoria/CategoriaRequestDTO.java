package com.ProyectoCarrito.categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaRequestDTO {
	
	@NotBlank(message = "El nombre es obligatorio")
	@Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
	private String nombre;

	@Size(max = 250, message = "La descripción no puede exceder los 250 caracteres")
	private String descripcion;
}
