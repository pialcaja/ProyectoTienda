package com.ProyectoCarrito.usuario;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	@Query("""
			SELECT u FROM Usuario u
			JOIN u.rol r
			WHERE (
				LOWER(u.nombre) LIKE LOWER(CONCAT('%', :filtro, '%')) OR
				LOWER(u.email) LIKE LOWER(CONCAT('%', :filtro, '%')) OR
				LOWER(r.nombre) LIKE LOWER(CONCAT('%', :filtro, '%'))
			)
			AND u.estado = :estado
			""")
	public Page<Usuario> buscarPorFiltro(
			@Param("filtro") String filtro, 
			@Param("estado") Boolean estado, 
			Pageable pageable);
	
	public Page<Usuario> findByEstado(Boolean estado, Pageable pageable);

	public Optional<Usuario> findByEmailIgnoreCase(String email);
	
	public Boolean existsByEmail(String email);

	public Boolean existsByEmailAndIdNot(String email, Long id);
}
