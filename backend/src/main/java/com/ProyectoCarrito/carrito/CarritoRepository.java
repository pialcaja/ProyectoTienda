package com.ProyectoCarrito.carrito;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {

	public Optional<Carrito> findByUsuarioId(Long usuarioId);
	
	@Query("""
			SELECT c FROM Carrito c 
			LEFT JOIN c.items 
			WHERE c.usuario.id = :usuarioId	
			""")
	public Optional<Carrito> buscarPorUsuarioIdConItems(
			@Param("usuarioId") Long usuarioId);
	
	public Boolean existsByUsuarioId(Long usuarioId);
}
