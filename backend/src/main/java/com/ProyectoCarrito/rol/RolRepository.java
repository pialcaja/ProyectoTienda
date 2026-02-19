package com.ProyectoCarrito.rol;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {

	public Optional<Rol> findByNombre(String nombre);
	
	public Boolean existsByNombre(String nombre);
}
