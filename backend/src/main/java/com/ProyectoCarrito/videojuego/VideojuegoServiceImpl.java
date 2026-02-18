package com.ProyectoCarrito.videojuego;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ProyectoCarrito.categoria.Categoria;
import com.ProyectoCarrito.categoria.CategoriaRepository;
import com.ProyectoCarrito.exception.ResourceNotFoundException;
import com.ProyectoCarrito.genero.Genero;
import com.ProyectoCarrito.genero.GeneroRepository;
import com.ProyectoCarrito.plataforma.Plataforma;
import com.ProyectoCarrito.plataforma.PlataformaRepository;
import com.ProyectoCarrito.producto.Producto;
import com.ProyectoCarrito.producto.ProductoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VideojuegoServiceImpl implements VideojuegoService {
	
	private final VideojuegoRepository videojuegoRepo;
	private final ProductoRepository productoRepo;
	private final CategoriaRepository categoriaRepo;
	private final GeneroRepository generoRepo;
	private final PlataformaRepository plataformaRepo;
	
	@Override
	@Transactional(readOnly = true)
	public Page<VideojuegoResponseDTO> listarConFiltros(Boolean estado, 
			Long generoId, Long plataformaId, String nombre,
			int page, int size, String sortBy, String sortDir) {
		
		// VALIDACION DE PARAMETROS DE PAGINADO
		if (page < 0) {
			page = 0;
		}
		
		if (size < 1 || size > 11) {
			size = 10;
		}
		
		// CREACION SORT (ORDEN)
		String campoOrden = mapearCampoOrden(sortBy);
		Sort sort = sortDir.equalsIgnoreCase("desc") ? 
				Sort.by(campoOrden).descending() : Sort.by(campoOrden).ascending();
		
		// CREACION PAGEABLE
		Pageable pageable = PageRequest.of(page, size, sort);
		
		// BUSQUEDA CON FILTROS
		Page<Videojuego> videojuegoPage = videojuegoRepo.buscarConFiltro(estado, 
				generoId, plataformaId, nombre, pageable);
		
		// MAPEO A DTO
		return videojuegoPage.map(this::mapearAResponseDTO);
	}

	@Override
	@Transactional(readOnly = true)
	public VideojuegoResponseDTO obtenerPorId(Long id, Boolean soloActivos) {
		
		Videojuego videojuego;
		
		if (soloActivos) {
			videojuego = videojuegoRepo.buscarPorIdActivo(id)
					.orElseThrow(() -> new ResourceNotFoundException("Registro de videojuego", "id", id));
		} else {
			videojuego = videojuegoRepo.buscarPorId(id)
					.orElseThrow(() -> new ResourceNotFoundException("Registro de videojuego", "id", id));
		}
		
		return mapearAResponseDTO(videojuego);
	}

	@Override
	@Transactional
	public VideojuegoResponseDTO crear(VideojuegoRequestDTO dto) {
		
		// VALIDA EXISTENCIA DE CATEGORIA
		Categoria categoria = categoriaRepo.findById(dto.getCategoriaId())
				.orElseThrow(() -> new ResourceNotFoundException("Registro de categoria", "id", dto.getCategoriaId()));
		
		// VALIDA EXISTENCIA DE GENEROS
		Set<Genero> generos = validarYObtenerGeneros(dto.getGenerosIds());
		
		// VALIDA EXISTENCIA DE PLATAFORMAS
		Set<Plataforma> plataformas = validarYObtenerPlataformas(dto.getPlataformasIds());
		
		// CREA EL PRODUCTO BASE
		Producto producto = new Producto();
		producto.setNombre(dto.getNombre());
		producto.setDescripcion(dto.getDescripcion());
		producto.setPrecio(dto.getPrecio());
		producto.setStock(dto.getStock());
		producto.setImagenUrl(dto.getImagenUrl());
		producto.setCategoria(categoria);
		producto.setEstado(true);
		
		producto = productoRepo.save(producto);
		
		// CREA EL VIDEOJUEGO ENLAZADO AL PRODUCTO
		Videojuego videojuego = new Videojuego();
		videojuego.setProducto(producto);
		videojuego.setDesarrollador(dto.getDesarrollador());
		videojuego.setFechaLanzamiento(dto.getFechaLanzamiento());
		videojuego.setGeneros(generos);
		videojuego.setPlataformas(plataformas);
		
		videojuego = videojuegoRepo.save(videojuego);
		
		return mapearAResponseDTO(videojuego);
	}

	@Override
	@Transactional
	public VideojuegoResponseDTO actualizar(Long id, VideojuegoRequestDTO dto) {

		// VALIDAR EXISTENCIA DEL VIDEOJUEGO
		Videojuego videojuego = videojuegoRepo.buscarPorIdActivo(id)
				.orElseThrow(() -> new ResourceNotFoundException("Registro de videojuego", "id", id));
		
		// OBTENER EL PRODUCTO ENLAZADO
		Producto producto = videojuego.getProducto();
		
		// VALIDAR GENEROS Y PLATAFORMAS
		Set<Genero> generos = validarYObtenerGeneros(dto.getGenerosIds());
		Set<Plataforma> plataformas = validarYObtenerPlataformas(dto.getPlataformasIds());
		
		// ACTUALIZA EL PRODUCTO
		producto.setNombre(dto.getNombre());
		producto.setDescripcion(dto.getDescripcion());
		producto.setPrecio(dto.getPrecio());
		producto.setStock(dto.getStock());
		producto.setImagenUrl(dto.getImagenUrl());
		
		productoRepo.save(producto);
		
		// ACTUALIZA EL VIDEOJUEGO
		videojuego.setDesarrollador(dto.getDesarrollador());
		videojuego.setFechaLanzamiento(dto.getFechaLanzamiento());
		
		// ACTUALIZA LOS GENEROS Y PLATAFORMAS
		videojuego.getGeneros().clear();
		videojuego.getGeneros().addAll(generos);
		
		videojuego.getPlataformas().clear();
		videojuego.getPlataformas().addAll(plataformas);
		
		videojuegoRepo.save(videojuego);
		
		return mapearAResponseDTO(videojuego);
	}

	@Override
	@Transactional
	public void eliminar(Long id) {
		
		// VALIDAR EXISTENCIA DEL VIDEOJUEGO
		Videojuego videojuego = videojuegoRepo.buscarPorIdActivo(id)
				.orElseThrow(() -> new ResourceNotFoundException("Registro de videojuego", "id", id));
		
		// ELIMINACION LOGICA
		Producto producto = videojuego.getProducto();
		producto.setEstado(false);
		
		productoRepo.save(producto);
	}

	@Override
	@Transactional
	public void recuperar(Long id) {

		// VALIDAR EXISTENCIA DEL VIDEOJUEGO
		Videojuego videojuego = videojuegoRepo.buscarPorId(id)
				.orElseThrow(() -> new ResourceNotFoundException("Registro de videojuego", "id", id));
		
		// RECUPERACION LOGICA
		Producto producto = videojuego.getProducto();
		
		// VALIDAR QUE EL PRODUCTO ESTE ELIMINADO
		if (producto.getEstado()) {
			throw new IllegalStateException("El videojuego ya está activo");
		}
		
		producto.setEstado(true);
		
		productoRepo.save(producto);
	}

	private VideojuegoResponseDTO mapearAResponseDTO(Videojuego videojuego) {
		Producto producto = videojuego.getProducto();
		
		// EXTRAE LOS NOMBRES DE LOS GENEROS
		Set<String> generosNombres = videojuego.getGeneros().stream()
				.map(Genero::getNombre)
				.collect(Collectors.toSet());
		
		// EXTRAE LOS NOMBRES DE LAS PLATAFORMAS
		Set<String> plataformasNombres = videojuego.getPlataformas().stream()
				.map(Plataforma::getNombre)
				.collect(Collectors.toSet());
		
		return new VideojuegoResponseDTO(
				producto.getId(),
				producto.getNombre(),
				producto.getDescripcion(),
				producto.getPrecio(),
				producto.getStock(),
				producto.getImagenUrl(),
				producto.getCategoria().getNombre(),
				videojuego.getDesarrollador(),
				videojuego.getFechaLanzamiento(),
				generosNombres,
				plataformasNombres
		);
	}
	
	private Set<Genero> validarYObtenerGeneros(Set<Long> generosIds) {
		Set<Genero> generos = new HashSet<>();
		
		for (Long generoId : generosIds) {
			Genero genero = generoRepo.findById(generoId)
					.orElseThrow(() -> new ResourceNotFoundException("Registro de género", "id", generoId));
			
			generos.add(genero);
		}
		
		return generos;
	}
	
	private Set<Plataforma> validarYObtenerPlataformas(Set<Long> plataformasIds) {
		Set<Plataforma> plataformas = new HashSet<>();
		
		for (Long plataformaId : plataformasIds) {
			Plataforma plataforma = plataformaRepo.findById(plataformaId)
					.orElseThrow(() -> new ResourceNotFoundException("Registro de plataforma", "id", plataformaId));
			
			plataformas.add(plataforma);
		}
		
		return plataformas;
	}
	
	private String mapearCampoOrden(String sortBy) {

	    if (sortBy == null) return "producto.nombre";

	    return switch (sortBy) {
	        case "nombre" -> "producto.nombre";
	        case "precio" -> "producto.precio";
	        case "estado" -> "producto.estado";
	        case "fechaLanzamiento" -> "fechaLanzamiento";
	        case "desarrollador" -> "desarrollador";
	        default -> "producto.nombre";
	    };
	}
}
