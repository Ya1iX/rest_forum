package com.plnv.forum.service;

import com.plnv.forum.entity.Message;
import com.plnv.forum.entity.Topic;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TopicService extends Service<Topic, Long> {
    List<Message> readMessages(Long id, Topic entity, Pageable pageable);
}
