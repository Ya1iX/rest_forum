package com.plnv.forum.service;

import com.plnv.forum.entity.Section;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface SectionService extends Service<Section> {
    Section readById(Long id);
    Section readAnyById(Long id);
    Section edit(Section entity, Long id);
    Section restoreById(Long id);
    Section deleteById(Long id);
    List<Section> readAll(
            Long id,
            String name,
            String description,
            String tags,
            Boolean isPinned,
            Boolean isSecured,
            String password,
            Boolean isHidden,
            Boolean isDeleted,
            LocalDateTime createdAt,
            LocalDateTime changedAt,
            String iconURL);
    List<Section> readAllHidden(Pageable pageable);
    void hardDeleteById(Long id);
}
