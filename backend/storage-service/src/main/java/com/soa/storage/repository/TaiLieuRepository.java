package com.soa.storage.repository;

import com.soa.storage.entity.TaiLieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaiLieuRepository extends JpaRepository<TaiLieu, Long> {
    List<TaiLieu> findByMaDeTai(String maDeTai);
}
