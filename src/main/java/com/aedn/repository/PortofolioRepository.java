package com.aedn.repository;

import java.util.UUID;
import org.springframework.stereotype.Repository;
import com.aedn.entity.Portofolio;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PortofolioRepository extends JpaRepository<Portofolio, UUID> {
}
