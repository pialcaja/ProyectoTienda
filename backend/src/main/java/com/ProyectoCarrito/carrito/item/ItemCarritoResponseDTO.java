package com.ProyectoCarrito.carrito.item;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCarritoResponseDTO {

	private Long itemId;
	private Long productoId;
	private String nombreProducto;
	private String imageUrl;
	private BigDecimal precioUnitario;
	private Integer cantidad;
	private BigDecimal subtotal;
}
