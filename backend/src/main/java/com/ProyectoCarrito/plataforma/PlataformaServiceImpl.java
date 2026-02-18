package com.ProyectoCarrito.plataforma;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ProyectoCarrito.exception.ConflictException;
import com.ProyectoCarrito.exception.ResourceNotFoundException;
import com.ProyectoCarrito.videojuego.VideojuegoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlataformaServiceImpl implements PlataformaService {
	
	private final PlataformaRepository plataformaRepo;
	private final VideojuegoRepository videojuegoRepo;
	
	@Override
	@Transactional(readOnly = true)
	public List<Plataforma> listar() {
		List<Plataforma> plataformas = plataformaRepo.findAll();
		
		return plataformas;
	}

	@Override
	@Transactional(readOnly = true)
	public Plataforma obtenerPorId(Long id) {
		Plataforma plataforma = plataformaRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Registro de plataforma", "id", id));
		
		return plataforma;
	}

	@Override
	@Transactional
	public Plataforma crear(PlataformaRequestDTO dto) {
		if (plataformaRepo.existsByNombre(dto.getNombre())) {
			throw new ConflictException("Ya existe una plataforma con nombre: " + dto.getNombre());
		}
		
		Plataforma plataforma = new Plataforma();
		plataforma.setNombre(dto.getNombre());
		
		plataformaRepo.save(plataforma);
		
		return plataforma;
	}

	@Override
	@Transactional
	public Plataforma actualizar(Long id, PlataformaRequestDTO dto) {
		Plataforma plataforma = plataformaRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Registro de plataforma", "id", id));
		
		if (plataformaRepo.existsByNombreAndIdNot(dto.getNombre(), id)) {
			throw new ConflictException("Ya existe una plataforma con nombre: " + dto.getNombre());
		}
		
		plataforma.setNombre(dto.getNombre());
		
		plataformaRepo.save(plataforma);
		
		return plataforma;
	}

	@Override
	@Transactional
	public void eliminar(Long id) {
		Plataforma plataforma = plataformaRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Registro de plataforma", "id", id));
		
		// VALIDAR QUE LA PLATAFORMA NO TENGA VIDEOJUEGOS ASOCIADOS
		if (videojuegoRepo.existsByPlataformasContaining(plataforma)) {
			throw new ConflictException("No se puede eliminar, hay videojuegos con esta plataforma");
		}
		
		plataformaRepo.delete(plataforma);
	}

}
