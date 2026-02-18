package com.ProyectoCarrito.producto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ProyectoCarrito.categoria.Categoria;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name ="tb_producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "nombre", nullable = false)
	private String nombre;

	@Column(name = "descripcion")
	private String descripcion;

	@Column(name = "precio", nullable = false)
	private BigDecimal precio;

	@Column(name = "stock", nullable = false)
	private Integer stock;

	@Column(name = "imagenUrl")
	private String imagenUrl;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "categoriaId", nullable = false)
	private Categoria categoria;

	@Column(name = "fechaRegistro", updatable = false)
	private LocalDateTime fechaRegistro;

	@Column(name = "fechaActualizacion")
	private LocalDateTime fechaActualizacion;

	@Column(name = "estado")
	private Boolean estado = true;
	
	@PrePersist
	protected void onCreate() {
		fechaRegistro = LocalDateTime.now();
	}
	
	@PreUpdate
	protected void onUpdate() {
		fechaActualizacion = LocalDateTime.now();
	}
}
