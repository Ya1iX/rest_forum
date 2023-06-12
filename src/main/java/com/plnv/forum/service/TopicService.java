package com.plnv.forum.service;

import com.plnv.forum.entity.Topic;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TopicService extends Service<Topic> {
    Topic readById(Long id, Topic entity);
    Topic edit(Topic entity, Long id);
    Topic setIsDeletedById(Long id, Boolean isDeleted);
    Topic setIsHiddenById(Long id, Boolean isHidden);
    List<Topic> readAllHidden(Pageable pageable);
    void hardDeleteById(Long id);
}
