package com.ProyectoCarrito.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

	private String token;
	private String tipo = "Bearer";
	private Long usuarioId;
	private String nombre;
	private String email;
	private String rol;
}
