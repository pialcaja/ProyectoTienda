package com.ProyectoCarrito.carrito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.ProyectoCarrito.carrito.item.ItemCarrito;
import com.ProyectoCarrito.usuario.Usuario;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_carrito")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Carrito {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "usuarioId", unique = true, nullable = false)
	private Usuario usuario;
	
	@Column(name = "fechaRegistro", updatable = false)
	private LocalDateTime fechaRegistro;
	
	@Column(name = "fechaActualizacion")
	private LocalDateTime fechaActualizacion;
	
	@OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ItemCarrito> items = new ArrayList<>();
	
	@PrePersist
	protected void onCreate() {
		fechaRegistro = LocalDateTime.now();
		fechaActualizacion = LocalDateTime.now();
	}
	
	@PreUpdate
	protected void onUpdate() {
		fechaActualizacion = LocalDateTime.now();
	}
}
