package com.ProyectoCarrito.orden.item;

import java.math.BigDecimal;

import com.ProyectoCarrito.orden.Orden;
import com.ProyectoCarrito.producto.Producto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_item_orden")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemOrden {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "ordenId", nullable = false)
	private Orden orden;
	
	@ManyToOne
	@JoinColumn(name = "productoId", nullable = false)
	private Producto producto;
	
	@Column(name = "cantidad", nullable = false)
	private Integer cantidad;
	
	@Column(name = "precioUnitario", nullable = false, precision = 10, scale = 2)
	private BigDecimal precioUnitario;

	@Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
	private BigDecimal subtotal;
}
