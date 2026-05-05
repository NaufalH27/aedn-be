package com.aedn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aedn.entity.Request;

import java.util.UUID;

public interface RequestRepository extends JpaRepository<Request, UUID> {
}
