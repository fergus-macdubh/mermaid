package com.vfasad.repo;

import com.vfasad.entity.Order;
import com.vfasad.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatusIn(OrderStatus...status);

    @Query(value = "SELECT DISTINCT" +
            "  o.id " +
            "FROM paint_order o " +
            "  JOIN paint_order_consume c ON c.order_fk = o.id " +
            "  JOIN product p ON p.id = c.product_id " +
            "WHERE o.status = 'CREATED' AND p.quantity < c.calculated_quantity", nativeQuery = true)
    List<BigInteger> findBlockedOrdersIds();

    Optional<Order> findById(Long id);

    List<Order> findByCompletedBetween(LocalDateTime from, LocalDateTime to);

    @Query(value = "SELECT DISTINCT" +
            "  o.* " +
            "FROM paint_order o " +
            "  JOIN paint_order_consume c ON c.order_fk = o.id " +
            "WHERE c.product_id = :productId AND o.created >= :from AND o.created <= :to", nativeQuery = true)
    List<Order> findByProductStartedBetween(
            @Param("productId") long productId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to);

    List<Order> findByCreatedBetweenOrStatus(LocalDateTime start, LocalDateTime end, OrderStatus status);

    List<Order> findByClientIdAndStatusIsNotIn(Long clientId, OrderStatus...status);
}
