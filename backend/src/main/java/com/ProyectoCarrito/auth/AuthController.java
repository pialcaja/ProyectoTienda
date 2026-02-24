package com.ProyectoCarrito.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService service;
	
	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(
			@Valid @RequestBody LoginRequest request) {
		Map<String, Object> response = new HashMap<>();
		LoginResponse loginResponse = service.login(request);
		
		response.put("message", "Login exitoso");
		response.put("data", loginResponse);
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/registro")
	public ResponseEntity<Map<String, Object>> registro(
			@Valid @RequestBody RegistroRequest request) {
		Map<String, Object> response = new HashMap<>();
		LoginResponse loginResponse = service.registro(request);
		
		response.put("message", "Usuario registrado exitosamente");
		response.put("data", loginResponse);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
