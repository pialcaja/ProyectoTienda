package com.ProyectoCarrito.carrito.item;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ActualizarCantidadRequestDTO {

	@NotNull(message = "La cantidad es obligatoria")
	@Min(value = 1, message = "La cantidad debe ser al menos 1")
	private Integer cantidad;
}
