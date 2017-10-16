package com.vfasad.repo;

import com.vfasad.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByQuantityGreaterThan(double quantityGraterThan);

    List<Product> findAllByOrderById();

    Optional<Product> findById(Long id);
}