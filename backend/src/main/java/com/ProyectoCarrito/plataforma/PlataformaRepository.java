package com.ProyectoCarrito.plataforma;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlataformaRepository extends JpaRepository<Plataforma, Long> {

	public Boolean existsByNombre(String nombre);

	public Boolean existsByNombreAndIdNot(String nombre, Long id);
}
