package com.ProyectoCarrito.producto;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // List<Producto> findByEstado(Boolean estado);
    
    Optional<Producto> findByIdAndEstado(Long id, Boolean estado);
    
    // List<Producto> findByCategoriaIdAndEstado(Long categoriaId, Boolean estado);
}
