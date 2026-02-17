package com.ProyectoCarrito.plataforma;

import java.util.List;

public interface PlataformaService {

	public List<Plataforma> listar();
	
	public Plataforma obtenerPorId(Long id);
	
	public Plataforma crear(PlataformaRequestDTO dto);
	
	public Plataforma actualizar(Long id, PlataformaRequestDTO dto);
	
	public void eliminar(Long id);
}
