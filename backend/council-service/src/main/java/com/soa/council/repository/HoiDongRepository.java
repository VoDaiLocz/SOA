package com.soa.council.repository;

import com.soa.council.entity.HoiDong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface HoiDongRepository extends JpaRepository<HoiDong, Long> {
    Optional<HoiDong> findByMaHoiDong(String maHoiDong);
    boolean existsByMaHoiDong(String maHoiDong);
}
