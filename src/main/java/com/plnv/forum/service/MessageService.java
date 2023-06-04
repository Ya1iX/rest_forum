package com.plnv.forum.service;

import com.plnv.forum.entity.Message;

import java.util.UUID;

public interface MessageService extends Service<Message> {
    Message readById(UUID id);
    void deleteById(UUID id);
}
