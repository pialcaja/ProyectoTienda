package com.ProyectoCarrito.orden;

import org.springframework.data.domain.Page;

public interface OrdenService {
	
	public Page<OrdenResponseDTO> listarOrdenesDelUsuario(
			Long usaurioId, Orden.EstadoOrden estado, int page, int size);
	
	public Page<OrdenResponseDTO> listarTodasOrdenes(
			String filtro, Orden.EstadoOrden estado, int page, int size);
	
	public OrdenResponseDTO obtenerOrden(
			Long ordenId, Long usuarioId, Boolean esAdmin);

	public OrdenResponseDTO crearOrden(
			Long usuarioId, CrearOrdenRequestDTO dto);
	
	public OrdenResponseDTO actualizarEstado(
			Long ordenId, ActualizarEstadoRequestDTO dto);
	
	public void cancelarOrden(Long ordenId, Long usuarioId, Boolean esAdmin);
}
