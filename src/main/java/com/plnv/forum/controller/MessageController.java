package com.plnv.forum.controller;

import com.plnv.forum.entity.Message;
import com.plnv.forum.service.MessageService;
import com.plnv.forum.service.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController extends AbstractController<Message, UUID> {
    private final MessageService service;

    @Override
    public Service<Message, UUID> getService() {
        return service;
    }
}
