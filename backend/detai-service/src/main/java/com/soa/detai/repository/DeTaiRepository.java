package com.soa.detai.repository;

import com.soa.detai.entity.DeTai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeTaiRepository extends JpaRepository<DeTai, Long> {
    Optional<DeTai> findByMaDeTai(String maDeTai);
    boolean existsByMaDeTai(String maDeTai);
    List<DeTai> findByTrangThai(String trangThai);
}
