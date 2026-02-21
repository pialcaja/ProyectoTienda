package com.ProyectoCarrito.orden;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/ordenes")
@RequiredArgsConstructor
public class OrdenController {

	private final OrdenService service;
	
	@GetMapping("/mis-ordenes/{usuarioId}")
	public ResponseEntity<Map<String, Object>> misOrdenes(
			@PathVariable Long usuarioId,
			@RequestParam(required = false) Orden.EstadoOrden estado,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		
		Map<String, Object> response = new HashMap<>();
		Page<OrdenResponseDTO> ordenes = service.listarOrdenesDelUsuario(
				usuarioId, estado, page, size);
		
		response.put("message", ordenes.getContent().isEmpty() ?
				"No se encontraron órdenes" : "Órdenes encontradas");
		response.put("ordenes", ordenes.getContent());
		response.put("paginaActual", ordenes.getNumber());
		response.put("totalPaginas", ordenes.getTotalPages());
		response.put("totalItems", ordenes.getTotalElements());
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/admin")
	public ResponseEntity<Map<String, Object>> listarTodasOrdenes(
			@RequestParam(required = false) String filtro,
			@RequestParam(required = false) Orden.EstadoOrden estado,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		
		Map<String, Object> response = new HashMap<>();
		Page<OrdenResponseDTO> ordenes = service.listarTodasOrdenes(
				filtro, estado, page, size);
		
		response.put("message", ordenes.getContent().isEmpty() ?
				"No se encontraron órdenes" : "Órdenes encontradas");
		response.put("ordenes", ordenes.getContent());
		response.put("paginaActual", ordenes.getNumber());
		response.put("totalPaginas", ordenes.getTotalPages());
		response.put("totalItems", ordenes.getTotalElements());
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/{ordenId}")
	public ResponseEntity<Map<String, Object>> obtenerOrden(
			@PathVariable Long ordenId,
			@RequestParam(required = false) Long usuarioId,
			@RequestParam(defaultValue = "false") Boolean esAdmin) {
		
		Map<String, Object> response = new HashMap<>();
		OrdenResponseDTO orden = service.obtenerOrden(
				ordenId, usuarioId, esAdmin);
		
		response.put("message", "Orden encontrada");
		response.put("orden", orden);
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/checkout/{usuarioId}")
	public ResponseEntity<Map<String, Object>> crearOrden(
			@PathVariable Long usuarioId,
			@Valid @RequestBody CrearOrdenRequestDTO dto) {
		
		Map<String, Object> response = new HashMap<>();
		OrdenResponseDTO orden = service.crearOrden(usuarioId, dto);
		
		response.put("message", "Orden creada correctamente");
		response.put("orden", orden);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@PatchMapping("/{ordenId}/estado")
	public ResponseEntity<Map<String, Object>> actualizarEstado(
			@PathVariable Long ordenId,
			@Valid @RequestBody ActualizarEstadoRequestDTO dto) {
		
		Map<String, Object> response = new HashMap<>();
		OrdenResponseDTO orden = service.actualizarEstado(ordenId, dto);
		
		response.put("message", "Estado actualizado correctamente");
		response.put("orden", orden);
		
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/{ordenId}")
	public ResponseEntity<Map<String, Object>> cancelarOrden(
			@PathVariable Long ordenId,
			@RequestParam(required = false) Long usuarioId,
			@RequestParam(defaultValue = "false") Boolean esAdmin) {
		
		Map<String, Object> response = new HashMap<>();
		service.cancelarOrden(ordenId, usuarioId, esAdmin);
		
		response.put("message", "Orden cancelada correctamente");
		
		return ResponseEntity.ok(response);
	}
}
