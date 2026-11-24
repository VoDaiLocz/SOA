package com.soa.council.repository;

import com.soa.council.entity.PhanCongDeTai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PhanCongDeTaiRepository extends JpaRepository<PhanCongDeTai, Long> {
    List<PhanCongDeTai> findByMaHoiDong(String maHoiDong);
}
