package com.ProyectoCarrito.videojuego;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/api/videojuegos")
@RequiredArgsConstructor
public class VideojuegoController {

	public final VideojuegoService service;
	
	@GetMapping
	public ResponseEntity<Map<String, Object>> listar(
			@RequestParam(required = false) Boolean estado,
			@RequestParam(required = false) Long generoId,
			@RequestParam(required = false) Long plataformaId,
			@RequestParam(required = false) String nombre,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "nombre") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDir) {
		
		Map<String, Object> response = new HashMap<>();
		Page<VideojuegoResponseDTO> videojuegosPage = service.listarConFiltros(
				estado, generoId, plataformaId, nombre, page, size, sortBy, sortDir);
		
		response.put("message", videojuegosPage.getContent().isEmpty() ?
				"No se encontraron registros con los filtros ingresados" :
				"Videojuegos encontrados");
		response.put("videojuegos", videojuegosPage.getContent());
		response.put("paginaActual", videojuegosPage.getNumber());
		response.put("totalItems", videojuegosPage.getTotalElements());
		response.put("totalPaginas", videojuegosPage.getTotalPages());
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> obtenerPorId(
			@PathVariable Long id,
			@RequestParam(defaultValue = "true") Boolean soloActivos) {
		
		Map<String, Object> response = new HashMap<>();
		VideojuegoResponseDTO videojuego = service.obtenerPorId(id, soloActivos);
		
		response.put("message", "Videojuego encontrado");
		response.put("videojuego", videojuego);
		
		return ResponseEntity.ok(response);
	}
	@PostMapping
	public ResponseEntity<Map<String, Object>> crear(
			@Valid @RequestBody VideojuegoRequestDTO dto) {
		
		Map<String, Object> response = new HashMap<>();
		VideojuegoResponseDTO videojuego = service.crear(dto);
		
		response.put("message", "Videojuego registrado correctamente");
		response.put("videojuego", videojuego);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> crear(
			@PathVariable Long id, 
			@RequestBody VideojuegoRequestDTO dto) {
		
		Map<String, Object> response = new HashMap<>();
		VideojuegoResponseDTO videojuego = service.actualizar(id, dto);
		
		response.put("message", "Videojuego actualizado correctamente");
		response.put("videojuego", videojuego);
		
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, Object>> eliminar(
			@PathVariable Long id) {
		
		Map<String, Object> response = new HashMap<>();
		service.eliminar(id);
		
		response.put("message", "Videojuego eliminado correctamente");
		
		return ResponseEntity.ok(response);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<Map<String, Object>> recuperar(
			@PathVariable Long id) {
		
		Map<String, Object> response = new HashMap<>();
		service.recuperar(id);
		
		response.put("message", "Videojuego recuperado correctamente");
		
		return ResponseEntity.ok(response);
	}
}
