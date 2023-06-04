package com.plnv.forum.service;

import com.plnv.forum.entity.Section;

public interface SectionService extends Service<Section> {
    Section readById(Long id);
    void deleteById(Long id);
}
