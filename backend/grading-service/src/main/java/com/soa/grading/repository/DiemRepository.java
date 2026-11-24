package com.soa.grading.repository;

import com.soa.grading.entity.Diem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DiemRepository extends JpaRepository<Diem, Long> {
    List<Diem> findByMaDeTai(String maDeTai);
    List<Diem> findByMaGv(String maGv);
}
