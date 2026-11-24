package com.soa.user.repository;

import com.soa.user.entity.Lop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LopRepository extends JpaRepository<Lop, Long> {
    List<Lop> findByMaKhoa(String maKhoa);
}
