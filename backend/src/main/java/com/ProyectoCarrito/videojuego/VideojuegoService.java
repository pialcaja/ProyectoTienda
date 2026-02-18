package com.ProyectoCarrito.videojuego;

import org.springframework.data.domain.Page;

public interface VideojuegoService {

	public Page<VideojuegoResponseDTO> listarConFiltros(Boolean estado, 
			Long generoId, Long plataformaId, String nombre,
			int page, int size, String sortBy, String sortDir);

	public VideojuegoResponseDTO obtenerPorId(Long id, Boolean soloActivos);

	public VideojuegoResponseDTO crear(VideojuegoRequestDTO dto);

	public VideojuegoResponseDTO actualizar(Long id, VideojuegoRequestDTO dto);

	public void eliminar(Long id);

	public void recuperar(Long id);
}
