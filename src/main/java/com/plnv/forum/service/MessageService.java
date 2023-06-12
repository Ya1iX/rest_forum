package com.plnv.forum.service;

import com.plnv.forum.entity.Message;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface MessageService extends Service<Message> {
    Message readById(UUID id, Message entity);
    Message edit(Message entity, UUID id);
    Message setIsDeletedById(UUID id, Boolean isDeleted);
    Message setIsHiddenById(UUID id, Boolean isHidden);
    List<Message> readAllHidden(Pageable pageable);
    void hardDeleteById(UUID id);
}
