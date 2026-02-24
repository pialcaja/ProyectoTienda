package com.ProyectoCarrito.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ProyectoCarrito.exception.BadRequestException;
import com.ProyectoCarrito.exception.ConflictException;
import com.ProyectoCarrito.rol.Rol;
import com.ProyectoCarrito.rol.RolRepository;
import com.ProyectoCarrito.security.CustomUserDetailsService;
import com.ProyectoCarrito.security.JwtTokenProvider;
import com.ProyectoCarrito.usuario.Usuario;
import com.ProyectoCarrito.usuario.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider tokenProvider;
	private final UsuarioRepository usuarioRepo;
	private final RolRepository rolRepo;
	private final PasswordEncoder passwordEncoder;
	private final CustomUserDetailsService userDetailsService;
	
	// AUTENTICA USUARIO Y GENERA TOKEN JWT
	public LoginResponse login(LoginRequest request) {
		// AUTENTICA CREDENCIALES
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.getEmail(), 
						request.getPwd())
		);
		
		// ESTABLECE AUTENTICACION EN EL CONTEXTO
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		// GENERA TOKEN JWT
		String token = tokenProvider.generarToken(authentication);
		
		// OBTIENEN DATOS DEL USUARIO
		Usuario usuario = userDetailsService.obtenerUsuarioPorEmail(request.getEmail());
		
		// CONSTRUYE RESPUESTA
		return new LoginResponse(
				token,
				"Bearer",
				usuario.getId(),
				usuario.getNombre(),
				usuario.getEmail(),
				usuario.getRol().getNombre()
		);
	}
	
	// REGISTRO USUARIO (CLIENTE)
	@Transactional
	public LoginResponse registro(RegistroRequest request) {
		// VALIDA QUE EL EMAIL NO EXISTA
		if (usuarioRepo.existsByEmail(request.getEmail())) {
			throw new ConflictException(
					"Ya existe un usuario con el email: " + request.getEmail());
		}
		
		// BUSCA EL ROL CLIENTE
		Rol rolCliente = rolRepo.findByNombre("CLIENTE")
				.orElseThrow(() -> new BadRequestException(
						"Rol CLIENTE no encontrado"));
		
		// CREA USUARIO
		Usuario usuario = new Usuario();
		usuario.setNombre(request.getNombre());
		usuario.setEmail(request.getEmail());
		usuario.setPwd(passwordEncoder.encode(request.getPwd()));
		usuario.setRol(rolCliente);
		usuario.setEstado(true);
		
		usuario = usuarioRepo.save(usuario);
		
		// HACE LOGIN AUTOMATICO DESPUES DEL REGISTRO
		LoginRequest loginRequest = new LoginRequest(
				request.getEmail(),
				request.getPwd()
		);
		
		return login(loginRequest);
	}
}
