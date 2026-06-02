package com.aedn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aedn.entity.Request;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RequestRepository extends JpaRepository<Request, UUID> {
    @Query("""
        SELECT DISTINCT r
        FROM Request r
        JOIN FETCH r.product p
        LEFT JOIN FETCH p.pictures
        LEFT JOIN FETCH r.order o
    """)
    List<Request> findAllWithProductPictures();

    @Query("""
        SELECT DISTINCT r
        FROM Request r
        JOIN FETCH r.product p
        LEFT JOIN FETCH p.pictures
        LEFT JOIN FETCH r.order o
        WHERE r.user.id = :userId
    """)
    List<Request> findWithProductPicturesByUserId(@Param("userId") UUID userId);

    @Query("""
        SELECT DISTINCT r
        FROM Request r
        JOIN FETCH r.product p
        LEFT JOIN FETCH p.pictures
        LEFT JOIN FETCH r.order o
        WHERE r.id = :id
    """)
    Optional<Request> findByIdWithProductPictures(@Param("id") UUID id);

    @Modifying
    @Query("""
        UPDATE Request r
        SET r.status = :newStatus
        WHERE r.id = :requestId
          AND r.status IN :currentStatuses
    """)
    int changeStatus(
        @Param("requestId") UUID orderId,
        @Param("currentStatuses") List<String> currentStatuses,
        @Param("newStatus") String newStatus
    );

    @Modifying
    @Query("""
        UPDATE Request r
        SET r.status = :newStatus
        WHERE r.id = :requestId
          AND r.status IN :currentStatuses
          AND r.user.id IN :userId
    """)
    int changeStatusByUserId(
        @Param("requestId") UUID orderId,
        @Param("userId") UUID userId,
        @Param("currentStatuses") List<String> currentStatuses,
        @Param("newStatus") String newStatus
    );
}
