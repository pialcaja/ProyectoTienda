package com.ProyectoCarrito.test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ProyectoCarrito.exception.BadRequestException;
import com.ProyectoCarrito.exception.ConflictException;
import com.ProyectoCarrito.exception.ResourceNotFoundException;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/test")
public class TestController {

	@GetMapping("/health")
	public Map<String, Object> health() {
		Map<String, Object> response = new HashMap<>();
		response.put("status", "UP");
		response.put("message", "API funcionando correctamente");
		
		return response;
	}
	
	@GetMapping("/error-404")
	public void testNotFound() {
		throw new ResourceNotFoundException("Producto", "id", 999);
	}
	
	@GetMapping("/error-400")
	public void testBadRequest() {
		throw new BadRequestException("El precio no puede ser negativo");
	}
	
	@GetMapping("/error-409")
	public void testConflict() {
		throw new ConflictException("Ya existe una categoria con ese nombre");
	}
	
	@GetMapping("/error-500")
	public void testServerError() {
		throw new RuntimeException("Error inesperado del servidor");
	}
	
	@GetMapping("/model-mapper")
	public String testModelMapper(@RequestParam(defaultValue = "ModelMapper") String name) {
		return "ModelMapper configurado correctamente para: " + name;
	}
}
