package com.ProyectoCarrito.categoria;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

	private final CategoriaService service;
	
	@GetMapping
	public ResponseEntity<Map<String, Object>> listar(
			@RequestParam(defaultValue = "true") Boolean estado) {
		Map<String, Object> response = new HashMap<>();
		List<Categoria> categorias = service.listar(estado);
		
		response.put("message", !categorias.isEmpty() ? "Listado de categorías" 
				: "No se encontraron registros");
		response.put("categorias", categorias);
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Categoria> obtener(@PathVariable Long id) {
		return ResponseEntity.ok(service.obtener(id));
	}
	
	@PostMapping
	public ResponseEntity<Map<String, Object>> crear(
			@Valid @RequestBody CategoriaRequestDTO dto) {
		Map<String, Object> response = new HashMap<>();
		Categoria categoria = service.crear(dto);
		
		response.put("message", "Categoría registrada correctamente");
		response.put("categoria", categoria);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> actualizar(
			@PathVariable Long id, 
			@Valid @RequestBody CategoriaRequestDTO dto) {
		Map<String, Object> response = new HashMap<>();
		Categoria categoria = service.actualizar(id, dto);
		
		response.put("message", "Categoría actualizada correctamente");
		response.put("categoria", categoria);
		
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, Object>> eliminar(
			@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		
		service.eliminar(id);
		
		response.put("message", "Categoría eliminada correctamente");
		
		return ResponseEntity.ok(response);
	}
	
	@PatchMapping("/recuperar/{id}")
	public ResponseEntity<Map<String, Object>> recuperar(
			@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		
		service.recuperar(id);
		
		response.put("message", "Categoría recuperada correctamente");
		
		return ResponseEntity.ok(response);
	}
}
