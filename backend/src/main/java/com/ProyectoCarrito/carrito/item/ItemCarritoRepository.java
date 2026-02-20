package com.ProyectoCarrito.carrito.item;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {

	public Optional<ItemCarrito> findByCarritoIdAndProductoId(
			Long carritoId, Long productoId);
	
	@Query("""
			SELECT i FROM ItemCarrito i 
			JOIN i.producto p 
			WHERE i.carrito.id = :carritoId 
			ORDER BY i.id DESC
			""")
	public Page<ItemCarrito> buscarPorCarrito(
			@Param("carritoId") Long carritoId, Pageable pageable);
	
	public Long countByCarritoId(Long carritoId);
	
	public void deleteByCarritoId(Long carritoId);
}
