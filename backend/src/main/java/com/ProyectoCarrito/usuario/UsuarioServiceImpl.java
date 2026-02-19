package com.ProyectoCarrito.usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ProyectoCarrito.exception.BadRequestException;
import com.ProyectoCarrito.exception.ConflictException;
import com.ProyectoCarrito.exception.ResourceNotFoundException;
import com.ProyectoCarrito.rol.Rol;
import com.ProyectoCarrito.rol.RolRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
	
	private final UsuarioRepository usuarioRepo;
	private final RolRepository rolRepo;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	@Transactional(readOnly = true)
	public Page<UsuarioResponseDTO> listarPorFiltro(String filtro, 
			Boolean estado, int page, int size, String sortBy, String sortDir) {
		
		// VALIDACION DE PARAMETROS DE PAGINADO
		if (page < 0) {
			page = 0;
		}
		
		if (size < 1 || size > 11) {
			size = 10;
		}
		
		// CREACION SORT
		String campoOrden = mapearCampoOrden(sortBy);
		Sort sort = sortDir.equalsIgnoreCase("desc") ?
				Sort.by(campoOrden).descending() : 
				Sort.by(campoOrden).ascending();
		
		// CREACION PAGEABLE
		Pageable pageable = PageRequest.of(page, size, sort);
		
		// BUSQUEDA POR FILTRO
		Page<Usuario> usuarioPage;
		
		if (filtro != null && !filtro.isEmpty()) {
			usuarioPage = usuarioRepo.buscarPorFiltro(filtro, 
					estado, pageable);
		} else {
			usuarioPage = usuarioRepo.findByEstado(estado, pageable);
		}
		
		// MAPEO A DTO DE RESPUESTA
		return usuarioPage.map(this::mapearAResponseDTO);
	}

	@Override
	@Transactional(readOnly = true)
	public Usuario obtenerPorId(Long id) {
		Usuario usuario = usuarioRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Registro de usuario", "id", id));
		
		return usuario;
	}

	@Override
	@Transactional
	public UsuarioResponseDTO crear(UsuarioRequestDTO dto) {

		// VALIDAR EXISTENCIA DEL EMAIL
		if (usuarioRepo.existsByEmail(dto.getEmail())) {
			throw new ConflictException(
					"Ya existe un usuario con el email: " + dto.getEmail());
		}
		
		// VALIDAR EXISTENCIA DEL ROL
		Rol rol = rolRepo.findById(dto.getRolId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Registro de rol", "id", dto.getRolId()));
		
		// VALIDACION DEL CAMPO PASSWORD
		if (dto.getPwd() == null || dto.getPwd().trim().isEmpty()) {
			throw new BadRequestException("La contraseña es obligatoria");
		}
		
		if (!dto.getPwd().matches(
				"^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{6,12}$")) {
			throw new BadRequestException(
					"La contraseña debe ser de 6 a 12 caracteres en total y"
					+ " debe contener la menos una mayúscula,"
					+ " un número, un caracter especial");
		}
		
		// CREACION DE USUARIO
		Usuario usuario = new Usuario();
		usuario.setNombre(dto.getNombre());
		usuario.setEmail(dto.getEmail());
		usuario.setPwd(passwordEncoder.encode(dto.getPwd()));
		usuario.setRol(rol);
		usuario.setEstado(true);
		
		usuario = usuarioRepo.save(usuario);
		
		return mapearAResponseDTO(usuario);
	}

	@Override
	@Transactional
	public UsuarioResponseDTO actualizar(Long id, UsuarioRequestDTO dto) {

		// VALIDAR EXISTENCIA DEL USUARIO
		Usuario usuario = usuarioRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Registro de usuario", "id", id));
		
		// VALIDAR EXISTENCIA DEL EMAIL
		if (usuarioRepo.existsByEmailAndIdNot(dto.getEmail(), id)) {
			throw new ConflictException(
					"Ya existe un usuario con el email: " + dto.getEmail());
		}
		
		// VALIDAR EXISTENCIA DEL ROL (ACTUALIZACION EN CASO DE CAMBIO)
		if (!usuario.getRol().getId().equals(dto.getRolId())) {
			Rol rol = rolRepo.findById(dto.getRolId())
					.orElseThrow(() -> new ResourceNotFoundException(
							"Registro de rol", "id", dto.getRolId()));
			
			usuario.setRol(rol);
		}
		
		// ACTUALIZACION DEL USUARIO
		usuario.setNombre(dto.getNombre());
		usuario.setEmail(dto.getEmail());
		
		// VALIDACION DEL CAMPO PASSWORD
		if (dto.getPwd() != null || !dto.getPwd().trim().isEmpty()) {
			if (!dto.getPwd().matches(
					"^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{6,12}$")) {
				throw new BadRequestException(
						"La contraseña debe ser de 6 a 12 caracteres en total y"
						+ " debe contener la menos una mayúscula,"
						+ " un número, un caracter especial");
			}
			usuario.setPwd(passwordEncoder.encode(dto.getPwd()));
		}
		
		usuario = usuarioRepo.save(usuario);
		
		return mapearAResponseDTO(usuario);
	}

	@Override
	@Transactional
	public void eliminar(Long id) {
		
		// VALIDAR EXISTENCIA DEL USUARIO
		Usuario usuario = usuarioRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Registro de usuario", "id", id));
		
		// VALIDACION DE ESTADO DEL USUARIO
		if (!usuario.getEstado()) {
			throw new ConflictException("El usuario ya está eliminado");
		}
		
		// ELIMINACION LOGICA
		usuario.setEstado(false);
		
		usuarioRepo.save(usuario);
	}

	@Override
	@Transactional
	public void recuperar(Long id) {

		// VALIDAR EXISTENCIA DEL USUARIO
		Usuario usuario = usuarioRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Registro de usuario", "id", id));
		
		// VALIDACION DE ESTADO DEL USUARIO
		if (usuario.getEstado()) {
			throw new ConflictException("El usuario ya está activo");
		}
		
		// ELIMINACION LOGICA
		usuario.setEstado(true);
		
		usuarioRepo.save(usuario);
	}

	private UsuarioResponseDTO mapearAResponseDTO(Usuario usuario) {
		return new UsuarioResponseDTO(
				usuario.getId(),
				usuario.getNombre(),
				usuario.getEmail(),
				usuario.getRol().getNombre(),
				usuario.getEstado()
		);
	}
	
	private String mapearCampoOrden(String sortBy) {
		if (sortBy == null) {
			return "usuario.nombre";
		}
		
		return switch (sortBy) {
		case "nombre" -> "nombre";
		case "email" -> "email";
		case "rol" -> "rol.nombre";
		default -> "nombre";
		};
	}
}
