package com.ProyectoCarrito.genero;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ProyectoCarrito.exception.ConflictException;
import com.ProyectoCarrito.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeneroServiceImpl implements GeneroService {
	
	private final GeneroRepository generoRepo;
	
	@Override
	@Transactional(readOnly = true)
	public List<Genero> listar() {
		// BUSCAR GENEROS
		List<Genero> generos = generoRepo.findAll();
		
		return generos;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Genero obtenerPorId(Long id) {
		// BUSCAR EL GENERO POR ID
		Genero genero = generoRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Registro de género", "id", id));
		
		return genero;
	}

	@Override
	@Transactional
	public Genero crear(GeneroRequestDTO dto) {
		// VALIDAR EXISTENCIA DEL GENERO
		if (generoRepo.existsByNombre(dto.getNombre())) {
			throw new ConflictException("Ya existe un género con el nombre: " + dto.getNombre());
		}
		
		Genero genero = new Genero();
		genero.setNombre(dto.getNombre());
		
		generoRepo.save(genero);
		
		return genero;
	}

	@Override
	@Transactional
	public Genero actualizar(Long id, GeneroRequestDTO dto) {
		// VALIDAR EXISTENCIA DEL GENERO
		Genero genero = generoRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Registro de género", "id", id));
		
		// VALIDAR DUPLICADO DE NOMBRE
		if (generoRepo.existsByNombreAndIdNot(dto.getNombre(), id)) {
			throw new ConflictException("Ya existe un género con el nombre: " + dto.getNombre());
		}
		
		genero.setNombre(dto.getNombre());
		
		generoRepo.save(genero);
		
		return genero;
	}

	@Override
	@Transactional
	public void eliminar(Long id) {
		// VALIDAR EXISTENCIA DEL GENERO
		Genero genero = generoRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Registro de género", "id", id));
		
		// VALIDAR QUE EL GENERO NO TENGA VIDEOJUEGOS ASOCIADOS
//		if (videojuegoRepo.existsByGenerosContaining(genero)) {
//			throw new ConflictException("No se puede eliminar, hay videojuegos con este género");
//		}
		
		generoRepo.delete(genero);
	}
}
