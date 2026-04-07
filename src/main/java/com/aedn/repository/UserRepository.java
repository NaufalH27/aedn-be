package com.aedn.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.aedn.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
}
