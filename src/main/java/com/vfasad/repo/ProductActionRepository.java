package com.vfasad.repo;

import com.vfasad.entity.ProductAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductActionRepository extends JpaRepository<ProductAction, Long> {
    List<ProductAction> findByproductId(Long productId);
}
