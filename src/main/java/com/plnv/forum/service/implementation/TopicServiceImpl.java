package com.plnv.forum.service.implementation;

import com.plnv.forum.entity.Topic;
import com.plnv.forum.repository.TopicRepository;
import com.plnv.forum.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {
    private final TopicRepository repository;

    @Override
    public List<Topic> readAll(Pageable pageable) {
        return repository.findAll(pageable).toList();
    }

    @Override
    public List<Topic> readAllDeleted(Pageable pageable) {
        return null;
    }

    @Override
    public Topic postNew(Topic entity) {
        return null;
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
