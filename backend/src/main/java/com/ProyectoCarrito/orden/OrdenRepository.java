package com.ProyectoCarrito.orden;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {

	@Query("""
			SELECT o FROM Orden o
			JOIN o.usuario u
			WHERE (:filtro IS NULL OR 
					LOWER(u.email) LIKE LOWER(CONCAT('%', :filtro, '%')) OR 
					CAST(o.id AS string) LIKE CONCAT('%', :filtro, '%'))
			AND (:estado IS NULL OR o.estado = :estado)
			AND (:usuarioId IS NULL OR u.id = :usuarioId)
			ORDER BY o.fechaOrden DESC
	""")
	Page<Orden> buscarConFiltros(
			@Param("filtro") String filtro,
			@Param("estado") Orden.EstadoOrden estado,
			@Param("usuarioId") Long usuarioId,
			Pageable pageable);
	
	@Query("""
			SELECT o FROM Orden o 
			LEFT JOIN o.items 
			WHERE o.id = :id
			""")
	Optional<Orden> buscarPorIdConItems(@Param("id") Long id);
	
	@Query("""
			SELECT o FROM Orden o 
			LEFT JOIN o.items 
			WHERE o.id = :id 
			AND o.usuario.id = :usuarioId
			""")
	Optional<Orden> buscarPorIdYUsuarioIdConItems(
			@Param("id") Long id, @Param("usuarioId") Long usuarioId);
}
