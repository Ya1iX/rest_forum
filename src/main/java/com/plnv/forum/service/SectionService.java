package com.plnv.forum.service;

import com.plnv.forum.entity.Section;
import com.plnv.forum.entity.Topic;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SectionService extends Service<Section, Long> {
    List<Topic> readTopics(Long id, Section entity, Topic topicEntity, Pageable pageable);
}
