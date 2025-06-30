package com.witalo.dscatalog.repositories;

import com.witalo.dscatalog.entites.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {

}
