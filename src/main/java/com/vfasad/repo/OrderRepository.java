package com.vfasad.repo;

import com.vfasad.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatusIn(Order.Status ...status);

    @Query(value = "SELECT DISTINCT\n" +
            "  o.id\n" +
            "FROM paint_order o\n" +
            "  JOIN paint_order_consume c ON c.order_fk = o.id\n" +
            "  JOIN product p ON p.id = c.product_id\n" +
            "WHERE o.status = 'CREATED' AND p.quantity >= c.calculated_quantity", nativeQuery = true)
    List<BigInteger> findBlockedOrdersIds();
}
