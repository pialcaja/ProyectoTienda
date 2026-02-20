package com.ProyectoCarrito.carrito;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ProyectoCarrito.carrito.item.ActualizarCantidadRequestDTO;
import com.ProyectoCarrito.carrito.item.AgregarItemRequestDTO;
import com.ProyectoCarrito.carrito.item.ItemCarritoResponseDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
public class CarritoController {

	private final CarritoService service;
	
	@GetMapping("/{usuarioId}")
	public ResponseEntity<Map<String, Object>> obtenerCarrito(
			@PathVariable Long usuarioId,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		
		Map<String, Object> response = new HashMap<>();
		CarritoResponseDTO carrito = service.obtenerCarrito(usuarioId, page, size);
		
		response.put("message", "Carrito obtenido correctamente");
		response.put("carrito", carrito);
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/{usuarioId}/items")
	public ResponseEntity<Map<String, Object>> agregarItem(
			@PathVariable Long usuarioId,
			@Valid @RequestBody AgregarItemRequestDTO dto) {
		
		Map<String, Object> response = new HashMap<>();
		ItemCarritoResponseDTO item = service.agregarItem(usuarioId, dto);
		
		response.put("message", "Producto agregado al carrito");
		response.put("item", item);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@PutMapping("/{usuarioId}/items/{itemId}")
	public ResponseEntity<Map<String, Object>> actualizarCantidad(
			@PathVariable Long usuarioId,
			@PathVariable Long itemId,
			@Valid @RequestBody ActualizarCantidadRequestDTO dto) {
		
		Map<String, Object> response = new HashMap<>();
		ItemCarritoResponseDTO item = service.actualizarCantidad(
				usuarioId, itemId, dto);
		
		response.put("message", "Cantidad actualizada");
		response.put("item", item);
		
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/{usuarioId}/items/{itemId}")
	public ResponseEntity<Map<String, Object>> eliminarItem(
			@PathVariable Long usuarioId,
			@PathVariable Long itemId) {
		
		Map<String, Object> response = new HashMap<>();
		service.eliminarItem(usuarioId, itemId);
		
		response.put("message", "Producto eliminado del carrito");
		
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/{usuarioId}")
	public ResponseEntity<Map<String, Object>> vaciarCarrito(
			@PathVariable Long usuarioId) {
		
		Map<String, Object> response = new HashMap<>();
		service.vaciarCarrito(usuarioId);
		
		response.put("message", "Carrito vaciado correctamente");
		
		return ResponseEntity.ok(response);
	}
}
