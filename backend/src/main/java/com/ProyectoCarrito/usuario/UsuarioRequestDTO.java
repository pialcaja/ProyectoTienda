package com.ProyectoCarrito.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioRequestDTO {

	@NotBlank(message = "El nombre es obligatorio")
	@Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
	private String nombre;
	
	@NotBlank(message = "El email es obligatorio")
	@Pattern(regexp = "^[A-Za-z0-9._-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)+$", 
			message = "Formato de email inválido")
	@Size(max = 100, message = "El email no puede exceder los 100 caracteres")
	private String email;
	
	private String pwd;
	
	@NotNull(message = "El rol es obligatorio")
	private Long rolId;
}
