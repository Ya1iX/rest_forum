package com.plnv.forum.service.implementation;

import com.plnv.forum.entity.Message;
import com.plnv.forum.repository.MessageRepository;
import com.plnv.forum.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private MessageRepository repository;

    @Override
    public Message readById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new NoSuchElementException("Message not found by id: " + id));
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public List<Message> list(int page, int size) {
        return repository.findAll(PageRequest.of(page, size)).toList();
    }

    @Override
    public List<Message> readAll() {
        return repository.findAll();
    }

    @Override
    public Message save(Message entity) {
        return repository.save(entity);
    }
}
