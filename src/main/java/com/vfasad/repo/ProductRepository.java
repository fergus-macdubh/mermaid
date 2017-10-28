package com.vfasad.repo;

import com.vfasad.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByQuantityGreaterThanOrderByName(double quantityGraterThan);

    List<Product> findAllByOrderById();

    Optional<Product> findById(Long id);

    @Query(value = "select sum(sp.sumPrice) " +
            "from (select p.price * p.quantity as sumPrice " +
            "       from Product p " +
            "       where p.quantity > 0) sp", nativeQuery = true)
    double getStoragePrice();
}