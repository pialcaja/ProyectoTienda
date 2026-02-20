package com.ProyectoCarrito.carrito;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ProyectoCarrito.carrito.item.ActualizarCantidadRequestDTO;
import com.ProyectoCarrito.carrito.item.AgregarItemRequestDTO;
import com.ProyectoCarrito.carrito.item.ItemCarrito;
import com.ProyectoCarrito.carrito.item.ItemCarritoRepository;
import com.ProyectoCarrito.carrito.item.ItemCarritoResponseDTO;
import com.ProyectoCarrito.exception.BadRequestException;
import com.ProyectoCarrito.exception.ResourceNotFoundException;
import com.ProyectoCarrito.producto.Producto;
import com.ProyectoCarrito.producto.ProductoRepository;
import com.ProyectoCarrito.usuario.Usuario;
import com.ProyectoCarrito.usuario.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CarritoServiceImpl implements CarritoService {
	
	private final CarritoRepository carritoRepo;
	private final ItemCarritoRepository itemCarritoRepo;
	private final UsuarioRepository usuarioRepo;
	private final ProductoRepository productoRepo;
	
	@Override
	@Transactional
	public CarritoResponseDTO obtenerCarrito(
			Long usuarioId, int page, int size) {
		
		// VALIDAR PAGINACION
		if (page < 0) {
			page = 0;
		}
		
		if (size < 1 || size > 50) {
			size = 10;
		}
		
		Pageable pageable = PageRequest.of(page, size);
		
		// OBTIENE O CREA NUEVO CARRITO
		Carrito carrito = carritoRepo.findByUsuarioId(usuarioId)
				.orElseGet(() -> crearCarritoVacio(usuarioId));
		
		// OBTIENE ITEMS PAGINADOS
		Page<ItemCarrito> itemsPage = itemCarritoRepo.buscarPorCarrito(
				carrito.getId(), pageable);
		
		// MAPEA EL CONTENIDO DE LA LISTA A DTO
		List<ItemCarritoResponseDTO> itemsDTO = itemsPage.getContent().stream()
				.map(this::mapearItemADTO).toList();
		
		// CALCULA TOTALES
		Long cantidadTotal = itemCarritoRepo.countByCarritoId(carrito.getId());
		BigDecimal totalCarrito = calcularTotalCarrito(carrito.getId());
		
		// CONSTRUYE RESPUESTA
		CarritoResponseDTO response = new CarritoResponseDTO();
		response.setCarritoId(carrito.getId());
		response.setUsuarioId(usuarioId);
		response.setCantidadTotalItems(cantidadTotal.intValue());
		response.setTotalCarrito(totalCarrito);
		response.setItems(itemsDTO);
		response.setPaginaActual(itemsPage.getNumber());
		response.setTotalPaginas(itemsPage.getTotalPages());
		response.setTotalItems(itemsPage.getTotalElements());
		
		return response;
	}

	@Override
	@Transactional
	public ItemCarritoResponseDTO agregarItem(
			Long usuarioId, AgregarItemRequestDTO dto) {
		
		// OBTIENE O CREA NUEVO CARRITO
		Carrito carrito = carritoRepo.findByUsuarioId(usuarioId)
				.orElseGet(() -> crearCarritoVacio(usuarioId));
		
		// VALIDA QUE EL PRODUCTO EXISTE Y ESTA ACTIVO
		Producto producto = productoRepo.findByIdAndEstado(
				dto.getProductoId(), true)
				.orElseThrow(() -> new ResourceNotFoundException(
				"Registro de usuario", "id", dto.getProductoId()));
		
		// VALIDA DISPONIBILIDAD DEL STOCK
		if (producto.getStock() < dto.getCantidad()) {
			throw new BadRequestException("Stock insuficiente. Disponible: " +
					producto.getStock());
		}
		
		// VERIFICA SI EL PRODUCTO YA ESTA EN EL CARRITO
		ItemCarrito item = itemCarritoRepo.findByCarritoIdAndProductoId(
				carrito.getId(), producto.getId())
				.orElse(null);
		
		if (item != null) {
			
			// ACTUALIZA LA CANTIDAD EXISTENTE
			int nuevaCantidad = item.getCantidad() + dto.getCantidad();
			
			if (nuevaCantidad > producto.getStock()) {
				throw new BadRequestException(
						"Stock insuficiente. Disponible: " +
						producto.getStock() + 
						", en carrito: " + 
						item.getCantidad());
			}
			
			item.setCantidad(nuevaCantidad);
			item.calcularSubtotal();
			
			item = itemCarritoRepo.save(item);
		} else {
			
			// CREA NUEVO ITEM
			item = new ItemCarrito();
			item.setCarrito(carrito);
			item.setProducto(producto);
			item.setCantidad(dto.getCantidad());
			item.setPrecioUnitario(producto.getPrecio());
			item.calcularSubtotal();
			
			item = itemCarritoRepo.save(item);
		}
		
		return mapearItemADTO(item);
	}

	@Override
	@Transactional
	public ItemCarritoResponseDTO actualizarCantidad(Long usuarioId, Long itemId, ActualizarCantidadRequestDTO dto) {
		
		// OBTIENE ITEM
		ItemCarrito item = itemCarritoRepo.findById(itemId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Registro de item", "id", itemId));
		
		// VALIDA QUE EL ITEM PERTENECE AL CARRITO DEL USUARIO
		if (!item.getCarrito().getUsuario().getId().equals(usuarioId)) {
			throw new BadRequestException("Este item no pertenece a tu carrito");
		}
		
		// VALIDA STOCK
		Producto producto = item.getProducto();
		if (dto.getCantidad() > producto.getStock()) {
			throw new BadRequestException("Stock insuficiente. Disponible: " +
					producto.getStock());
		}
		
		// ACTUALIZA ITEM
		item.setCantidad(dto.getCantidad());
		item.calcularSubtotal();
		
		item = itemCarritoRepo.save(item);
		
		return mapearItemADTO(item);
	}

	@Override
	@Transactional
	public void eliminarItem(Long usuarioId, Long itemId) {
		
		// OBTIENE ITEM
		ItemCarrito item = itemCarritoRepo.findById(itemId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Registro de item", "id", itemId));
		
		// VALIDA QUE EL ITEM PERTENECE AL CARRITO DEL USUARIO
		if (!item.getCarrito().getUsuario().getId().equals(usuarioId)) {
			throw new BadRequestException("Este item no pertenece a tu carrito");
		}
		
		itemCarritoRepo.delete(item);
	}

	@Override
	@Transactional
	public void vaciarCarrito(Long usuarioId) {
		
		// OBTIENE CARRITO DEL USUARIO
		Carrito carrito = carritoRepo.findByUsuarioId(usuarioId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Carrito del usuario", "id", usuarioId));
		
		itemCarritoRepo.deleteByCarritoId(carrito.getId());
	}

	private Carrito crearCarritoVacio(Long usuarioId) {
		
		// VALIDA EXISTENCIA DEL USUARIO
		Usuario usuario = usuarioRepo.findById(usuarioId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Registro de usuario", "id", usuarioId));
		
		// GENERA Y GUARDA CARRITO VACIO
		Carrito carrito = new Carrito();
		carrito.setUsuario(usuario);
		
		return carritoRepo.save(carrito);
	}
	
	private BigDecimal calcularTotalCarrito(Long carritoId) {
		
		return itemCarritoRepo.findAll().stream()
				.filter(item -> item.getCarrito().getId().equals(carritoId))
				.map(ItemCarrito::getSubtotal)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	private ItemCarritoResponseDTO mapearItemADTO(ItemCarrito item) {
		
		return new ItemCarritoResponseDTO(
				item.getId(),
				item.getProducto().getId(),
				item.getProducto().getNombre(),
				item.getProducto().getImagenUrl(),
				item.getPrecioUnitario(),
				item.getCantidad(),
				item.getSubtotal()
		);
	}
}
