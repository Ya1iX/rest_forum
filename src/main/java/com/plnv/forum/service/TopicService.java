package com.plnv.forum.service;

import com.plnv.forum.entity.Topic;

public interface TopicService extends Service<Topic> {
    Topic readById(Long id);
    void deleteById(Long id);
}
