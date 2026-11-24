package com.soa.notification.repository;

import com.soa.notification.entity.ThongBao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ThongBaoRepository extends JpaRepository<ThongBao, Long> {
    List<ThongBao> findByMaNguoiNhanOrderByNgayGuiDesc(String maNguoiNhan);
}
