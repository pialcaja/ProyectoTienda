package com.ProyectoCarrito.exception;

// EXCEPCION PARA CUANDO LA PETICION TIENE DATOS INVALIDOS
public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BadRequestException(String message) {
		super(message);
	}
}
