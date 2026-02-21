package com.ProyectoCarrito.orden;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ProyectoCarrito.carrito.Carrito;
import com.ProyectoCarrito.carrito.CarritoRepository;
import com.ProyectoCarrito.carrito.item.ItemCarrito;
import com.ProyectoCarrito.carrito.item.ItemCarritoRepository;
import com.ProyectoCarrito.exception.BadRequestException;
import com.ProyectoCarrito.exception.ResourceNotFoundException;
import com.ProyectoCarrito.orden.Orden.EstadoOrden;
import com.ProyectoCarrito.orden.item.ItemOrden;
import com.ProyectoCarrito.orden.item.ItemOrdenResponseDTO;
import com.ProyectoCarrito.producto.Producto;
import com.ProyectoCarrito.producto.ProductoRepository;
import com.ProyectoCarrito.usuario.Usuario;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrdenServiceImpl implements OrdenService {
	
	private final OrdenRepository ordenRepo;
	private final CarritoRepository carritoRepo;
	private final ItemCarritoRepository itemCarritoRepo;
	private final ProductoRepository productoRepo;
	
	@Override
	@Transactional(readOnly = true)
	public Page<OrdenResponseDTO> listarOrdenesDelUsuario(
			Long usuarioId, EstadoOrden estado, int page, int size) {
		
		// VALIDA PAGINADO
		if (page < 0) {
			page = 0;
		}
		
		if (size < 1 || size > 11) {
			size = 10;
		}
		
		Pageable pageable = PageRequest.of(page, size);
		
		Page<Orden> ordenesPage = ordenRepo.buscarConFiltros(
				null,
				estado,
				usuarioId,
				pageable
		);
		
		return ordenesPage.map(this::mapearAResponseDTO);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<OrdenResponseDTO> listarTodasOrdenes(
			String filtro, EstadoOrden estado, int page, int size) {
		
		// VALIDA PAGINADO
		if (page < 0) {
			page = 0;
		}
		
		if (size < 1 || size > 11) {
			size = 10;
		}
		
		Pageable pageable = PageRequest.of(page, size);
		
		Page<Orden> ordenesPage = ordenRepo.buscarConFiltros(
				filtro,
				estado,
				null,
				pageable
		);
		
		return ordenesPage.map(this::mapearAResponseDTO);
	}
	
	@Override
	@Transactional(readOnly = true)
	public OrdenResponseDTO obtenerOrden(
			Long ordenId, Long usuarioId, Boolean esAdmin) {
		
		Orden orden;
		
		// BUSCA ORDEN DEPENDIENDO DEL ROL
		if (esAdmin) {	
			orden = ordenRepo.buscarPorIdConItems(ordenId)
					.orElseThrow(() -> new ResourceNotFoundException(
							"Orden", "id", ordenId));
		} else {
			orden = ordenRepo.buscarPorIdYUsuarioIdConItems(ordenId, usuarioId)
					.orElseThrow(() -> new ResourceNotFoundException(
							"Orden", "id", ordenId));
		}
		
		return mapearAResponseDTOConItems(orden);
	}
	
	@Override
	@Transactional
	public OrdenResponseDTO crearOrden(
			Long usuarioId, CrearOrdenRequestDTO dto) {
		
		// BUSCA CARRITO DEL USUARIO
		Carrito carrito = carritoRepo.buscarPorUsuarioIdConItems(usuarioId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Carrito del usuario", "id", usuarioId));
		
		// VALIDA QUE EL CARRITO NO ESTE VACIO
		if (carrito.getItems().isEmpty()) {
			throw new BadRequestException("El carrito está vacío");
		}
		
		// VALIDA EL ESTADO Y STOCK DE TODOS LOS PRODUCTOS
		for (ItemCarrito itemCarrito : carrito.getItems()) {
			
			Producto producto = itemCarrito.getProducto();
			
			if (!producto.getEstado()) {
				throw new BadRequestException(
						"El producto '" + producto.getNombre() + 
						"' ya no está disponible");
			}
			
			if (producto.getStock() < itemCarrito.getCantidad()) {
				throw new BadRequestException("Stock insuficiente para "
						+ "'" + producto.getNombre() + "'"
						+ ". Disponible: " + producto.getStock() +
						", solicitado: " + itemCarrito.getCantidad());
			}
		}
		
		// CREA LA ORDEN
		Orden orden = new Orden();
		orden.setUsuario(carrito.getUsuario());
		orden.setDireccionEnvio(dto.getDireccionEnvio());
		orden.setMetodoPago(dto.getMetodoPago());
		orden.setEstado(Orden.EstadoOrden.PENDIENTE);
		
		// AGREGA ITEMS DEL CARRITO A LA ORDEN
		BigDecimal total = BigDecimal.ZERO;
		List<ItemOrden> itemsOrden = new ArrayList<>();
		
		for (ItemCarrito itemCarrito : carrito.getItems()) {
			
			ItemOrden itemOrden = new ItemOrden();
			itemOrden.setOrden(orden);
			itemOrden.setProducto(itemCarrito.getProducto());
			itemOrden.setCantidad(itemCarrito.getCantidad());
			itemOrden.setPrecioUnitario(itemCarrito.getPrecioUnitario());
			itemOrden.setSubtotal(itemCarrito.getSubtotal());
			
			itemsOrden.add(itemOrden);
			total = total.add(itemCarrito.getSubtotal());
		}
		
		orden.setTotal(total);
		orden.setItems(itemsOrden);
		
		// GUARDA LA ORDEN
		orden = ordenRepo.save(orden);
		
		// DESCUENTA STOCK DE LOS PRODUCTOS DE LA ORDEN
		for (ItemCarrito itemCarrito : carrito.getItems()) {
			
			Producto producto = itemCarrito.getProducto();
			producto.setStock(producto.getStock() - itemCarrito.getCantidad());
			
			productoRepo.save(producto);
		}
		
		// VACIA EL CARRITO
		itemCarritoRepo.deleteByCarritoId(carrito.getId());
		
		return mapearAResponseDTO(orden);
	}

	@Override
	@Transactional
	public OrdenResponseDTO actualizarEstado(
			Long ordenId, ActualizarEstadoRequestDTO dto) {
		
		// OBTIENE ORDEN
		Orden orden = ordenRepo.findById(ordenId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Orden", "id", ordenId));
		
		// VALIDAR ESTADO
		if (orden.getEstado() == Orden.EstadoOrden.CANCELADO ||
				orden.getEstado() == Orden.EstadoOrden.COMPLETADO) {
			throw new BadRequestException(
					"No se puede modificar una orden completada o cancelada");
		}
		
		orden.setEstado(dto.getEstado());
		
		orden = ordenRepo.save(orden);
		
		return mapearAResponseDTO(orden);
	}

	@Override
	@Transactional
	public void cancelarOrden(Long ordenId, Long usuarioId, Boolean esAdmin) {
		
		Orden orden;
		
		// BUSCA ORDEN DEPENDIENDO DEL ROL
		if (esAdmin) {	
			orden = ordenRepo.buscarPorIdConItems(ordenId)
					.orElseThrow(() -> new ResourceNotFoundException(
							"Orden", "id", ordenId));
		} else {
			orden = ordenRepo.buscarPorIdYUsuarioIdConItems(ordenId, usuarioId)
					.orElseThrow(() -> new ResourceNotFoundException(
							"Orden", "id", ordenId));
		}
		
		// VALIDA QUE LA ORDEN SE PUEDA CANCELAR
		if (orden.getEstado() == Orden.EstadoOrden.COMPLETADO) {
			throw new BadRequestException(
					"No se puede cancelar una orden completada");
		}
		
		if (orden.getEstado() == Orden.EstadoOrden.CANCELADO) {
			throw new BadRequestException(
					"La orden ya está cancelada");
		}
		
		// DEVUELVE EL STOCK A LOS PRODUCTOS
		for (ItemOrden item : orden.getItems()) {
			Producto producto = item.getProducto();
			producto.setStock(producto.getStock() + item.getCantidad());
			
			productoRepo.save(producto);
		}
		
		orden.setEstado(Orden.EstadoOrden.CANCELADO);
		
		ordenRepo.save(orden);
	}

	private OrdenResponseDTO mapearAResponseDTO(Orden orden) {
		
		Usuario usuario = orden.getUsuario();
		
		return new OrdenResponseDTO(
				orden.getId(), 
				usuario.getNombre(), 
				orden.getTotal(), 
				orden.getEstado().name(), 
				orden.getFechaOrden(), 
				orden.getMetodoPago().name(), 
				orden.getDireccionEnvio(), 
				orden.getItems().size(), 
				null
		);
	}
	
	private OrdenResponseDTO mapearAResponseDTOConItems(Orden orden) {
		
		Usuario usuario = orden.getUsuario();
		
		List<ItemOrdenResponseDTO> itemsDTO = orden.getItems().stream()
				.map(item -> new ItemOrdenResponseDTO(
						item.getProducto().getNombre(),
						item.getProducto().getImagenUrl(),
						item.getCantidad(),
						item.getPrecioUnitario(),
						item.getSubtotal()
				)).collect(Collectors.toList());
		
		return new OrdenResponseDTO(
				orden.getId(), 
				usuario.getNombre(), 
				orden.getTotal(), 
				orden.getEstado().name(), 
				orden.getFechaOrden(), 
				orden.getMetodoPago().name(), 
				orden.getDireccionEnvio(), 
				orden.getItems().size(), 
				itemsDTO
		);
	}
}
