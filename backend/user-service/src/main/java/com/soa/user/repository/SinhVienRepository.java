package com.soa.user.repository;

import com.soa.user.entity.SinhVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SinhVienRepository extends JpaRepository<SinhVien, Long> {
    Optional<SinhVien> findByMaSv(String maSv);
    boolean existsByMaSv(String maSv);
    void deleteByMaSv(String maSv);
}
