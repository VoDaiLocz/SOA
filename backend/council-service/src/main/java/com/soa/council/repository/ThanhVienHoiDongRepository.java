package com.soa.council.repository;

import com.soa.council.entity.ThanhVienHoiDong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ThanhVienHoiDongRepository extends JpaRepository<ThanhVienHoiDong, Long> {
    List<ThanhVienHoiDong> findByMaHoiDong(String maHoiDong);
}
