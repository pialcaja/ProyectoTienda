package com.ProyectoCarrito.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroRequest {

	@NotBlank(message = "El nombre es obligatorio")
	@Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
	private String nombre;
	
	@NotBlank(message = "El email es obligatorio")
	@Pattern(regexp = "^[A-Za-z0-9._-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)+$", 
	message = "Formato de email inválido")
	@Size(max = 100, message = "El email no puede exceder los 100 caracteres")
	private String email;
	
	@NotBlank(message = "La contraseña es obligatoria")
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{6,12}$", 
	message = "Formato de email inválido")
	private String pwd;
}
