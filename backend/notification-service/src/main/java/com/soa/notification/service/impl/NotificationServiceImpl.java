package com.soa.notification.service.impl;

import com.soa.notification.dto.NotificationDTOs.*;
import com.soa.notification.entity.ThongBao;
import com.soa.notification.repository.ThongBaoRepository;
import com.soa.notification.service.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final ThongBaoRepository thongBaoRepository;

    public NotificationServiceImpl(ThongBaoRepository thongBaoRepository) {
        this.thongBaoRepository = thongBaoRepository;
    }

    private ThongBaoDTO toDto(ThongBao tb) {
        return new ThongBaoDTO(
                tb.getId(),
                tb.getMaNguoiNhan(),
                tb.getTieuDe(),
                tb.getNoiDung(),
                tb.getLoaiThongBao(),
                tb.getDaDoc(),
                tb.getNgayGui()
        );
    }

    @Override
    public List<ThongBaoDTO> getThongBaoByNguoiNhan(String maNguoiNhan) {
        return thongBaoRepository.findByMaNguoiNhanOrderByNgayGuiDesc(maNguoiNhan)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void danhDauDaDoc(Long id) {
        thongBaoRepository.findById(id).ifPresent(tb -> {
            tb.setDaDoc(true);
            thongBaoRepository.save(tb);
        });
    }

    @Override
    @Transactional
    public ThongBaoDTO guiThongBao(GuiThongBaoRequest req) {
        ThongBao tb = new ThongBao(
                req.getMaNguoiNhan(),
                req.getTieuDe(),
                req.getNoiDung(),
                req.getLoaiThongBao()
        );
        return toDto(thongBaoRepository.save(tb));
    }
}
