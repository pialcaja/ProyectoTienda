package com.ProyectoCarrito.carrito;

import java.math.BigDecimal;
import java.util.List;

import com.ProyectoCarrito.carrito.item.ItemCarritoResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarritoResponseDTO {

	private Long carritoId;
	private Long usuarioId;
	private Integer cantidadTotalItems;
	private BigDecimal totalCarrito;
	private List<ItemCarritoResponseDTO> items;
	private Integer paginaActual;
	private Integer totalPaginas;
	private Long totalItems;
}
