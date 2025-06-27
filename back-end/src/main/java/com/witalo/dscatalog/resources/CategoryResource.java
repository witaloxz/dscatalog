package com.witalo.dscatalog.resources;

import java.util.List;

import com.witalo.dscatalog.dto.CategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.witalo.dscatalog.entites.Category;
import com.witalo.dscatalog.services.CategoryService;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {
	
	@Autowired
	private CategoryService service;
	
	@GetMapping
	public ResponseEntity<List<CategoryDTO>> findAll(){
		List<CategoryDTO> list = service.findAll();
		return ResponseEntity.ok().body(list);
		
	}
	@GetMapping("/{id}")
	public ResponseEntity<CategoryDTO> finById(@PathVariable Long id ){
			CategoryDTO dto = service.findByid(id);
			return ResponseEntity.ok().body(dto);
	}
}