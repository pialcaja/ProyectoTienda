package com.ProyectoCarrito.genero;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneroRepository extends JpaRepository<Genero, Long> {

	public Boolean existsByNombre(String nombre);
	
	public Boolean existsByNombreAndIdNot(String nombre, Long id);
}
