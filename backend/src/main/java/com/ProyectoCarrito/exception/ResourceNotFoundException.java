package com.ProyectoCarrito.exception;

// EXCEPCION PARA CUANDO UN RECURSO NO ES ENCONTRADO
public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ResourceNotFoundException(String message) {
		super(message);
	}

	public ResourceNotFoundException(String resource, String field, Object value) {
		super(String.format("%s no encontrado con %s: '%s'", resource, field, value));
	}
}
