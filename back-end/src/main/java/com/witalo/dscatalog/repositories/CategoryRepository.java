package com.witalo.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.witalo.dscatalog.entites.Category;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
