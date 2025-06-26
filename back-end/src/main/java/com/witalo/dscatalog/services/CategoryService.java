package com.witalo.dscatalog.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.witalo.dscatalog.entites.Category;
import com.witalo.dscatalog.repositories.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	CategoryRepository repository;
	
	public List<Category> findAll(){
		return repository.findAll();
	}
}
