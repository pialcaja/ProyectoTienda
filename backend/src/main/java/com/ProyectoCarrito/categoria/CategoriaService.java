package com.ProyectoCarrito.categoria;

import java.util.List;

public interface CategoriaService {

	public List<Categoria> listar(Boolean estado);
	
	public Categoria obtener(Long id);
	
	public Categoria crear(CategoriaRequestDTO dto);
	
	public Categoria actualizar(Long id, CategoriaRequestDTO dto);
	
	public void eliminar(Long id);
	
	public void recuperar(Long id);
}
