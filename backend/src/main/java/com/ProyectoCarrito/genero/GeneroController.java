package com.ProyectoCarrito.genero;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/generos")
@RequiredArgsConstructor
public class GeneroController {

	private final GeneroService service;
	
	@GetMapping
	public ResponseEntity<Map<String, Object>> listar() {
		Map<String, Object> response = new HashMap<>();
		List<Genero> generos = service.listar();
		
		response.put("message", generos.isEmpty() ? 
				"No se encontraron registros" : "Géneros encontrados");
		response.put("generos", generos);
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> buscar(
			@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		Genero genero = service.buscar(id);
		
		response.put("message", "Género encontrado");
		response.put("genero", genero);
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping
	public ResponseEntity<Map<String, Object>> crear(
			@Valid @RequestBody GeneroRequestDTO dto) {
		Map<String, Object> response = new HashMap<>();
		Genero genero = service.crear(dto);
		
		response.put("message", "Género registrado correctamente");
		response.put("genero", genero);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> actualizar(
			@PathVariable Long id, 
			@Valid @RequestBody GeneroRequestDTO dto) {
		Map<String, Object> response = new HashMap<>();
		Genero genero = service.actualizar(id, dto);
		
		response.put("message", "Género actualizado correctamente");
		response.put("genero", genero);
		
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, Object>> eliminar(
			@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		service.eliminar(id);
		
		response.put("message", "Género eliminado correctamente");
		
		return ResponseEntity.ok(response);
	}
}
