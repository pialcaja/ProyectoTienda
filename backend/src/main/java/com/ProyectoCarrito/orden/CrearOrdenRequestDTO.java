package com.ProyectoCarrito.orden;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearOrdenRequestDTO {

	@NotBlank(message = "La dirección de envío es obligatoria")
	@Size(max = 250, message = "La dirección de envío "
			+ "no puede exceder los 250 caracteres")
	private String direccionEnvio;
	
	@NotNull(message = "El método de pago es obligatorio")
	private Orden.MetodoPago metodoPago;
}
