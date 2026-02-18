package com.ProyectoCarrito.videojuego;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.ProyectoCarrito.genero.Genero;
import com.ProyectoCarrito.plataforma.Plataforma;
import com.ProyectoCarrito.producto.Producto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_videojuego")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Videojuego {

	@Id
	@Column(name = "productoId")
	private Long id;
	
	@OneToOne
	@MapsId
	@JoinColumn(name = "productoId")
	private Producto producto;
	
	@Column(name = "desarrollador")
	private String desarrollador;
	
	@Column(name = "fechaLanzamiento", nullable = false)
	private LocalDate fechaLanzamiento;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "tb_videojuego_genero",
			joinColumns = @JoinColumn(name = "videojuegoId"),
			inverseJoinColumns = @JoinColumn(name = "generoId")
	)
	private Set<Genero> generos = new HashSet<>();
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "tb_videojuego_plataforma",
			joinColumns = @JoinColumn(name = "videojuegoId"),
			inverseJoinColumns = @JoinColumn(name = "plataformaId")
	)
	private Set<Plataforma> plataformas = new HashSet<>();
}
