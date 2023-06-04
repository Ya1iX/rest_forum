package com.plnv.forum.service.implementation;

import com.plnv.forum.entity.Topic;
import com.plnv.forum.repository.TopicRepository;
import com.plnv.forum.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {
    private TopicRepository repository;

    @Override
    public List<Topic> list(int page, int size) {
        return repository.findAll(PageRequest.of(page, size)).toList();
    }

    @Override
    public List<Topic> readAll() {
        return repository.findAll();
    }

    @Override
    public Topic save(Topic entity) {
        return repository.save(entity);
    }

    @Override
    public Topic readById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NoSuchElementException("Topic not found by id: " + id));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
