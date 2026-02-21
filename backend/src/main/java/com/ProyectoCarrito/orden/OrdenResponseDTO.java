package com.ProyectoCarrito.orden;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.ProyectoCarrito.orden.item.ItemOrdenResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdenResponseDTO {

	private Long ordenId;
	private String nombreUsuario;
	private BigDecimal total;
	private String estado;
	private LocalDateTime fechaOrden;
	private String metodoPago;
	private String direccionEnvio;
	private Integer cantidadItems;
	private List<ItemOrdenResponseDTO> items;
}
