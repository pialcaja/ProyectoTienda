package com.ProyectoCarrito.usuario;

import org.springframework.data.domain.Page;

public interface UsuarioService {

	public Page<UsuarioResponseDTO> listarPorFiltro(String filtro, 
			Boolean estado,int page, int size, String sortBy, String sortDir);
	
	public Usuario obtenerPorId(Long id);
	
	public UsuarioResponseDTO crear(UsuarioRequestDTO dto);
	
	public UsuarioResponseDTO actualizar(Long id, UsuarioRequestDTO dto);

	public void eliminar(Long id);

	public void recuperar(Long id);
}
