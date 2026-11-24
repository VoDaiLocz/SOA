package com.soa.detai.repository;

import com.soa.detai.entity.DangKy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DangKyRepository extends JpaRepository<DangKy, Long> {
    List<DangKy> findByMaDeTai(String maDeTai);
    List<DangKy> findByMaSv(String maSv);
    boolean existsByMaSvAndMaDeTai(String maSv, String maDeTai);
    Optional<DangKy> findByMaSvAndMaDeTai(String maSv, String maDeTai);
}
