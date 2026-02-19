package com.ProyectoCarrito.usuario;

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
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

	private final UsuarioService service;
	
	@GetMapping
	public ResponseEntity<Map<String, Object>> listar(
			@RequestParam(required = false) String filtro,
			@RequestParam(defaultValue = "true") Boolean estado,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "nombre") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDir) {
		
		Map<String, Object> response = new HashMap<>();
		Page<UsuarioResponseDTO> usuarios = service.listarPorFiltro(
				filtro, estado, page, size, sortBy, sortDir);
		
		response.put("message", usuarios.getContent().isEmpty() ?
				"No se encontraron registros con los filtros ingresados" :
				"Usuarios encontrados");
		response.put("usuarios", usuarios.getContent());
		response.put("paginaActual", usuarios.getNumber());
		response.put("totalItems", usuarios.getTotalElements());
		response.put("totalPaginas", usuarios.getTotalPages());
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> obtenerPorId(
			@PathVariable Long id) {
		
		Map<String, Object> response = new HashMap<>();
		Usuario usuario = service.obtenerPorId(id);
		
		response.put("message", "Usuario encontrado");
		response.put("usuario", usuario);
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping
	public ResponseEntity<Map<String, Object>> crear(
			@Valid @RequestBody UsuarioRequestDTO dto) {
		
		Map<String, Object> response = new HashMap<>();
		UsuarioResponseDTO usuario = service.crear(dto);
		
		response.put("message", "Usuario registrado correctamente");
		response.put("usuario", usuario);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> actualizar(
			@PathVariable Long id, 
			@Valid @RequestBody UsuarioRequestDTO dto) {
		
		Map<String, Object> response = new HashMap<>();
		UsuarioResponseDTO usuario = service.actualizar(id, dto);
		
		response.put("message", "Usuario actualizado correctamente");
		response.put("usuario", usuario);
		
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, Object>> eliminar(
			@PathVariable Long id) {
		
		Map<String, Object> response = new HashMap<>();
		service.eliminar(id);
		
		response.put("message", "Usuario eliminado correctamente");
		
		return ResponseEntity.ok(response);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<Map<String, Object>> recuperar(
			@PathVariable Long id) {
		
		Map<String, Object> response = new HashMap<>();
		service.recuperar(id);
		
		response.put("message", "Usuario recuperado correctamente");
		
		return ResponseEntity.ok(response);
	}
}
