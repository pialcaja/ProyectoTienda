package com.ProyectoCarrito.videojuego;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ProyectoCarrito.genero.Genero;
import com.ProyectoCarrito.plataforma.Plataforma;

@Repository
public interface VideojuegoRepository extends JpaRepository<Videojuego, Long> {

    @Query("SELECT v FROM Videojuego v " +
            "LEFT JOIN v.producto p " +
            "LEFT JOIN p.categoria c " +
            "LEFT JOIN v.generos g " +
            "LEFT JOIN v.plataformas pl " +
            "WHERE (:estado IS NULL OR p.estado = :estado) " +
            "AND (:generoId IS NULL OR g.id = :generoId) " +
            "AND (:plataformaId IS NULL OR pl.id = :plataformaId) " +
            "AND (:nombre IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))")
	Page<Videojuego> buscarConFiltro(
			@Param("estado") Boolean estado,
			@Param("generoId") Long generoId,
			@Param("plataformaId") Long plataformaId,
			@Param("nombre") String nombre,
			Pageable pageable
	);
	
    @Query("SELECT v FROM Videojuego v " +
            "LEFT JOIN v.producto p " +
            "LEFT JOIN p.categoria " +
            "LEFT JOIN v.generos " +
            "LEFT JOIN v.plataformas " +
            "WHERE v.id = :id")
	Optional<Videojuego> buscarPorId(@Param("id") Long id);
	
    @Query("SELECT v FROM Videojuego v " +
            "LEFT JOIN v.producto p " +
            "LEFT JOIN p.categoria " +
            "LEFT JOIN v.generos " +
            "LEFT JOIN v.plataformas " +
            "WHERE v.id = :id AND p.estado = true")
	Optional<Videojuego> buscarPorIdActivo(@Param("id") Long id);
    
    Boolean existsByGenerosContaining(Genero genero);
    
    Boolean existsByPlataformasContaining(Plataforma plataforma);
}
