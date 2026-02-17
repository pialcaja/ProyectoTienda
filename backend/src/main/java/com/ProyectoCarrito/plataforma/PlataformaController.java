package com.ProyectoCarrito.plataforma;

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
@RequestMapping("/api/plataformas")
@RequiredArgsConstructor
public class PlataformaController {

	private final PlataformaService service;
	
	@GetMapping
	public ResponseEntity<Map<String, Object>> listar() {
		Map<String, Object> response = new HashMap<>();
		List<Plataforma> plataformas = service.listar();
		
		response.put("message", plataformas.isEmpty() ? 
				"No se encontraron registros" : "Plataformas encontradas");
		response.put("plataformas", plataformas);
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> obtenerPorId(
			@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		Plataforma plataforma = service.obtenerPorId(id);
		
		response.put("message", "Plataforma encontrada");
		response.put("plataforma", plataforma);
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping
	public ResponseEntity<Map<String, Object>> crear(
			@Valid @RequestBody PlataformaRequestDTO dto) {
		Map<String, Object> response = new HashMap<>();
		Plataforma plataforma = service.crear(dto);
		
		response.put("message", "Plataforma registrada correctamente");
		response.put("plataforma", plataforma);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> actualizar(
			@PathVariable Long id, 
			@Valid @RequestBody PlataformaRequestDTO dto) {
		Map<String, Object> response = new HashMap<>();
		Plataforma plataforma = service.actualizar(id, dto);
		
		response.put("message", "Plataforma actualizada correctamente");
		response.put("plataforma", plataforma);
		
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, Object>> eliminar(
			@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		service.eliminar(id);
		
		response.put("message", "Plataforma eliminada correctamente");
		
		return ResponseEntity.ok(response);
	}
}
