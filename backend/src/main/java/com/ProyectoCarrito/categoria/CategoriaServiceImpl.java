package com.ProyectoCarrito.categoria;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ProyectoCarrito.exception.BadRequestException;
import com.ProyectoCarrito.exception.ConflictException;
import com.ProyectoCarrito.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

	private final CategoriaRepository categoriaRepo;
	private final ModelMapper modelMapper;

	@Override
	@Transactional(readOnly = true)
	public List<Categoria> listar(Boolean estado) {
		List<Categoria> categorias = categoriaRepo.findByEstado(estado);

		return categorias;
	}

	@Override
	@Transactional(readOnly = true)
	public Categoria obtener(Long id) {
		Categoria categoria = categoriaRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Registro de categoría", "id", id));

		return categoria;
	}

	@Override
	@Transactional
	public Categoria crear(CategoriaRequestDTO dto) {
		if (categoriaRepo.existsByNombre(dto.getNombre())) {
			throw new ConflictException("Ya existe una categoría con el nombre: " + dto.getNombre());
		}

		Categoria categoria = modelMapper.map(dto, Categoria.class);
		categoria.setEstado(true);

		categoriaRepo.save(categoria);

		return categoria;
	}

	@Override
	@Transactional
	public Categoria actualizar(Long id, CategoriaRequestDTO dto) {
		Categoria categoria = categoriaRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Registro de categoría", "id", id));

		if (categoriaRepo.existsByNombreAndIdNot(dto.getNombre(), id)) {
			throw new ConflictException("Ya existe una categoría con el nombre: " + dto.getNombre());
		}

		categoria.setNombre(dto.getNombre());
		categoria.setDescripcion(dto.getDescripcion());

		categoriaRepo.save(categoria);

		return categoria;
	}

	@Override
	@Transactional
	public void eliminar(Long id) {
		Categoria categoria = categoriaRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Registro de categoría", "id", id));

			if (!categoria.getEstado()) {
				throw new BadRequestException("La categoría seleccionada ya se encuentra eliminada");
			}

		categoria.setEstado(false);

		categoriaRepo.save(categoria);
	}

	@Override
	@Transactional
	public void recuperar(Long id) {
		Categoria categoria = categoriaRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Registro de categoría", "id", id));

		if (categoria.getEstado()) {
			throw new BadRequestException("La categoría seleccionada ya se encuentra recuperada");
		}

		categoria.setEstado(true);

		categoriaRepo.save(categoria);
	}
}
