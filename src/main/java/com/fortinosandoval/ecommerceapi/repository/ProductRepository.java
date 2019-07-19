package com.fortinosandoval.ecommerceapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import com.fortinosandoval.ecommerceapi.models.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	void deleteById(Long i);
	
	Optional<Product> findById(Long i);

}
