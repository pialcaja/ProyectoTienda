package com.ProyectoCarrito.carrito;

import com.ProyectoCarrito.carrito.item.ActualizarCantidadRequestDTO;
import com.ProyectoCarrito.carrito.item.AgregarItemRequestDTO;
import com.ProyectoCarrito.carrito.item.ItemCarritoResponseDTO;

public interface CarritoService {

	public CarritoResponseDTO obtenerCarrito(
			Long usuarioId, int page, int size);
	
	public ItemCarritoResponseDTO agregarItem(
			Long usuarioId, AgregarItemRequestDTO dto);
	
	public ItemCarritoResponseDTO actualizarCantidad(
			Long usuarioId, Long itemId, ActualizarCantidadRequestDTO dto);
	
	public void eliminarItem(
			Long usuarioId, Long itemId);
	
	public void vaciarCarrito(Long usuarioId);
}
