package com.soa.analytics.repository;

import com.soa.analytics.entity.SoLieuTongHop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SoLieuTongHopRepository extends JpaRepository<SoLieuTongHop, Long> {
    Optional<SoLieuTongHop> findTopByOrderByNgayTongHopDesc();
}
