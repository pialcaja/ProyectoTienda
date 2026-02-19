package com.ProyectoCarrito.usuario;

import java.time.LocalDateTime;

import com.ProyectoCarrito.rol.Rol;

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
@Table(name = "tb_usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "nombre", nullable = false)
	private String nombre;

	@Column(name = "email", unique = true, nullable = false)
	private String email;

	@Column(name = "pwd", nullable = false)
	private String pwd;

	@Column(name = "fechaRegistro", updatable = false)
	private LocalDateTime fechaRegistro;

	@Column(name = "fechaActualizacion", nullable = false)
	private LocalDateTime fechaActualizacion;

	@Column(name = "estado")
	private Boolean estado = true;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "rolId", nullable = false)
	private Rol rol;
	
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
