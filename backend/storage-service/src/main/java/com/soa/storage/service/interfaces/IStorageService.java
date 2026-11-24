package com.soa.storage.service.interfaces;

import com.soa.storage.dto.StorageDTOs.TaiLieuDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface IStorageService {
    TaiLieuDTO uploadFile(String maDeTai, String loaiTaiLieu, String nguoiUpload, MultipartFile file);
    List<TaiLieuDTO> getTaiLieuByDeTai(String maDeTai);
    Resource downloadFile(Long id);
    TaiLieuDTO getTaiLieuById(Long id);
    void deleteFile(Long id);
}
