package com.ProyectoCarrito.exception;

//EXCEPCION PARA CUANDO HAY CONFLICTO DE DATOS
public class ConflictException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ConflictException(String message) {
		super(message);
	}
}
