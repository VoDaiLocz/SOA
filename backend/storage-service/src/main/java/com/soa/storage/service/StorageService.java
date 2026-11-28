package com.soa.storage.service;

import com.soa.storage.dto.StorageDTOs.TaiLieuDTO;
import com.soa.storage.entity.TaiLieu;
import com.soa.storage.repository.TaiLieuRepository;
import com.soa.storage.service.interfaces.IStorageService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StorageService implements IStorageService {

    private static final Logger log = LoggerFactory.getLogger(StorageService.class);
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("pdf", "doc", "docx", "zip", "rar", "txt", "xlsx");

    private final TaiLieuRepository taiLieuRepository;

    @Value("${app.storage.upload-dir}")
    private String uploadDir;

    private Path rootPath;

    public StorageService(TaiLieuRepository taiLieuRepository) {
        this.taiLieuRepository = taiLieuRepository;
    }

    @PostConstruct
    public void init() {
        try {
            rootPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(rootPath);
        } catch (IOException e) {
            log.error("Could not initialize storage directory", e);
        }
    }

    private TaiLieuDTO toDto(TaiLieu tl) {
        return new TaiLieuDTO(
                tl.getId(),
                tl.getMaDeTai(),
                tl.getTenFile(),
                tl.getDuongDan(),
                tl.getLoaiTaiLieu(),
                tl.getKichThuoc(),
                tl.getNguoiUpload(),
                tl.getNgayUpload()
        );
    }

    private String sanitizeAndValidateFilename(String rawFilename) {
        String cleaned = StringUtils.cleanPath(rawFilename != null ? rawFilename : "");
        String baseName = Paths.get(cleaned).getFileName().toString();
        if (baseName.isBlank() || baseName.contains("..")) {
            throw new IllegalArgumentException("Tên tệp không hợp lệ");
        }
        int dotIdx = baseName.lastIndexOf('.');
        String ext = dotIdx >= 0 ? baseName.substring(dotIdx + 1).toLowerCase() : "";
        if (!ALLOWED_EXTENSIONS.contains(ext)) {
            throw new IllegalArgumentException("Định dạng tệp không được hỗ trợ. Chỉ chấp nhận: " + ALLOWED_EXTENSIONS);
        }
        return baseName;
    }

    private Path resolveFilePath(String storedPath) {
        Path path = Paths.get(storedPath);
        Path resolved = path.isAbsolute() ? path.normalize() : rootPath.resolve(path).normalize();
        if (!path.isAbsolute() && !resolved.startsWith(rootPath)) {
            throw new SecurityException("Đường dẫn tệp không hợp lệ");
        }
        return resolved;
    }

    @Override
    @Transactional
    public TaiLieuDTO uploadFile(String maDeTai, String loaiTaiLieu, String nguoiUpload, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Không thể tải lên file rỗng");
        }

        String originalFilename = sanitizeAndValidateFilename(file.getOriginalFilename());
        String savedFilename = UUID.randomUUID() + "_" + originalFilename;

        try {
            Path targetLocation = resolveFilePath(savedFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            TaiLieu tl = new TaiLieu(
                    maDeTai,
                    originalFilename,
                    savedFilename,
                    loaiTaiLieu != null ? loaiTaiLieu : "THUYET_MINH",
                    file.getSize(),
                    nguoiUpload
            );

            return toDto(taiLieuRepository.save(tl));
        } catch (IOException e) {
            throw new RuntimeException("Lỗi lưu trữ file: " + e.getMessage(), e);
        }
    }

    @Override
    public List<TaiLieuDTO> getTaiLieuByDeTai(String maDeTai) {
        return taiLieuRepository.findByMaDeTai(maDeTai).stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public TaiLieuDTO getTaiLieuById(Long id) {
        return taiLieuRepository.findById(id).map(this::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài liệu: " + id));
    }

    @Override
    public Resource downloadFile(Long id) {
        TaiLieu tl = taiLieuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài liệu: " + id));

        try {
            Path filePath = resolveFilePath(tl.getDuongDan());
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            }
            throw new RuntimeException("File không tồn tại trên hệ thống lưu trữ");
        } catch (MalformedURLException e) {
            throw new RuntimeException("Đường dẫn file không hợp lệ", e);
        }
    }

    @Override
    @Transactional
    public void deleteFile(Long id) {
        TaiLieu tl = taiLieuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài liệu: " + id));

        try {
            Files.deleteIfExists(resolveFilePath(tl.getDuongDan()));
        } catch (IOException e) {
            log.warn("Không thể xóa file vật lý: {}", e.getMessage());
        }

        taiLieuRepository.delete(tl);
    }
}
