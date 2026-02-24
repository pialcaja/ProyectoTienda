package com.ProyectoCarrito.security;

import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ProyectoCarrito.usuario.Usuario;
import com.ProyectoCarrito.usuario.UsuarioRepository;

import lombok.RequiredArgsConstructor;

// CARGA USUARIO DESDE LA BD

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	
	private final UsuarioRepository usuarioRepo;
	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		Usuario usuario = usuarioRepo.findByEmailIgnoreCase(email)
				.orElseThrow(() -> new UsernameNotFoundException(
						"Usuario no encontrado con email: " + email));
		
		// VERIFICA QUE EL USUARIO ESTE ACTIVO
		if (!usuario.getEstado()) {
			throw new UsernameNotFoundException("Usuario inactivo");
		}
		
		String rolConPrefijo = "ROLE_" + usuario.getRol().getNombre();
		
		List<GrantedAuthority> authorities = Collections.singletonList(
				new SimpleGrantedAuthority(rolConPrefijo));
		
		return new User(
				usuario.getEmail(),
				usuario.getPwd(),
				usuario.getEstado(),
				true,
				true,
				true,
				authorities
		);
	}

	// OBTIENE EL USUARIO COMPLETO POR EMAIL
	@Transactional(readOnly = true)
	public Usuario obtenerUsuarioPorEmail(String email) {
		return usuarioRepo.findByEmailIgnoreCase(email)
				.orElseThrow(() -> new UsernameNotFoundException(
						"Usuario no encontrado con email: " + email));
	}
}
