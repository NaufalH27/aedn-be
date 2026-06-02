package com.aedn.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aedn.entity.OrderDrawingProgress;


@Repository
public interface OrderDrawingProgressRepository extends JpaRepository<OrderDrawingProgress, UUID> {
}
