package com.ProyectoCarrito.categoria;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
	
	public List<Categoria> findByEstado(Boolean estado);
	
	public Boolean existsByNombre(String nombre);
	
	public Boolean existsByNombreAndIdNot(String nombre, Long id);
}
