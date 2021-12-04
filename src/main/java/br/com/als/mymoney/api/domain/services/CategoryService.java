package br.com.als.mymoney.api.domain.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.als.mymoney.api.domain.model.Category;
import br.com.als.mymoney.api.domain.model.dto.CategoryDTO;
import br.com.als.mymoney.api.domain.model.dto.CategoryDTOInsert;
import br.com.als.mymoney.api.domain.repositories.CategoryRepository;
import br.com.als.mymoney.api.domain.services.exceptions.ObjectNotFoundException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	//TODO Implementar modelMapper na classe
	public CategoryDTO findByCodeOrThrow(String code) {
		Category obj = repository.findByCode(code)
				.orElseThrow(() -> new ObjectNotFoundException("Categoria não encontrada"));

		CategoryDTO objDTO = new CategoryDTO();
		BeanUtils.copyProperties(obj, objDTO);

		return objDTO;
	}

	public Category findByCodeOrThrowAsCategory(String code) {
		Category obj = repository.findByCode(code)
				.orElseThrow(() -> new ObjectNotFoundException("Categoria não encontrada"));

		return obj;
	}

	public List<CategoryDTO> findAll() {
		var list = repository.findAll();
		List<CategoryDTO> listDTO = list.stream().map(CategoryDTO::new).collect(Collectors.toList());
		return listDTO;
	}

	public CategoryDTO saveNew(CategoryDTOInsert objDTOInsert) {
		Category obj = new Category();
		BeanUtils.copyProperties(objDTOInsert, obj);
		Category newObj = repository.save(obj);
		CategoryDTO objDTO = new CategoryDTO();
		BeanUtils.copyProperties(newObj, objDTO);
		return objDTO;
	}

}
