package com.soa.user.repository;

import com.soa.user.entity.GiangVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface GiangVienRepository extends JpaRepository<GiangVien, Long> {
    Optional<GiangVien> findByMaGv(String maGv);
    boolean existsByMaGv(String maGv);
}
