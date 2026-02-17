package com.ProyectoCarrito.genero;

import java.util.List;

public interface GeneroService {

	public List<Genero> listar();
	
	public Genero obtenerPorId(Long id);
	
	public Genero crear(GeneroRequestDTO dto);
	
	public Genero actualizar(Long id, GeneroRequestDTO dto);
	
	public void eliminar(Long id);
}
