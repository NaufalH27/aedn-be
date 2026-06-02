package com.aedn.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aedn.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("""
        SELECT DISTINCT o
        FROM Order o
        JOIN FETCH o.request r
        LEFT JOIN FETCH r.product p
        LEFT JOIN FETCH p.pictures
    """)
    List<Order> findAllWithProductPictures();
    @Query("""
        SELECT DISTINCT o
        FROM Order o
        JOIN FETCH o.request r
        LEFT JOIN FETCH r.product p
        LEFT JOIN FETCH p.pictures
        WHERE r.user.id = :userId
    """)
    List<Order> findAllWithProductPicturesByUserId(@Param("userId") UUID userId);

    @Modifying
    @Query("""
        UPDATE Order o
        SET o.status = 'in_progress',
            o.paidStatus = 'skipped'
        WHERE o.id = :orderId
          AND o.status = 'pending_payment'
    """)
    int proceedWithoutPayment(@Param("orderId") UUID orderId);

    @Modifying
    @Query("""
        UPDATE Order o
        SET o.status = 'done'
        WHERE o.id = :orderId
          AND o.status = 'in_progress'
    """)
    int markAsDone(@Param("orderId") UUID orderId);
}
