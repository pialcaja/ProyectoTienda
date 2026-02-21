package com.ProyectoCarrito.orden;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.ProyectoCarrito.orden.item.ItemOrden;
import com.ProyectoCarrito.usuario.Usuario;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_orden")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Orden {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuarioId", nullable = false)
	private Usuario usuario;
	
	@Column(name = "total", nullable = false, precision = 10, scale = 2)
	private BigDecimal total;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "estado", nullable = false)
	private EstadoOrden estado;
	
	@Column(name = "fechaOrden", updatable = false)
	private LocalDateTime fechaOrden;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "metodoPago", nullable = false)
	private MetodoPago metodoPago;
	
	@Column(name = "direccionEnvio", nullable = false)
	private String direccionEnvio;
	
	@OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ItemOrden> items = new ArrayList<>();
	
	@PrePersist
	protected void onCreate() {
		fechaOrden = LocalDateTime.now();
		if (estado == null) {
			estado = EstadoOrden.PENDIENTE;
		}
	}
	
	public enum EstadoOrden {
		PENDIENTE,
		PAGADO,
		COMPLETADO,
		CANCELADO
	}
	
	public enum MetodoPago {
		MERCADOPAGO,
		TRANSFERENCIA,
		YAPE_PLIN
	}
}
