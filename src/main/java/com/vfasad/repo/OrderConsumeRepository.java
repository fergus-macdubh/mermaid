package com.vfasad.repo;

import com.vfasad.dto.OrderConsume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderConsumeRepository extends JpaRepository<OrderConsume, Long> {
}
