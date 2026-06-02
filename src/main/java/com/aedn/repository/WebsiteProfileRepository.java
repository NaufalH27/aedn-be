package com.aedn.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aedn.entity.WebsiteProfile;

@Repository
public interface WebsiteProfileRepository extends JpaRepository<WebsiteProfile, UUID> {
    Optional<WebsiteProfile> findFirstBy();
}
