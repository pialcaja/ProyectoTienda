package com.ProyectoCarrito.orden.item;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemOrdenResponseDTO {

	private String nombreProducto;
	private String imageUrl;
	private Integer cantidad;
	private BigDecimal precioUnitario;
	private BigDecimal subtotal;
}
