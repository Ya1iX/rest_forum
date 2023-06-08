package com.plnv.forum.service;

import com.plnv.forum.entity.Section;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SectionService extends Service<Section> {
    Section readById(Long id);
    Section edit(Section entity, Long id);
    Section setIsDeletedById(Long id, Boolean isDeleted);
    Section setIsHiddenById(Long id, Boolean isHidden);
    List<Section> readAllHidden(Pageable pageable);
    void hardDeleteById(Long id);
}
