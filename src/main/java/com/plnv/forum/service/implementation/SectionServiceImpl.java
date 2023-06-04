package com.plnv.forum.service.implementation;

import com.plnv.forum.entity.Section;
import com.plnv.forum.repository.SectionRepository;
import com.plnv.forum.service.SectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService {
    private SectionRepository repository;

    @Override
    public Section readById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NoSuchElementException("Section not found by id: " + id));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Section> list(int page, int size) {
        return repository.findAll(PageRequest.of(page, size)).toList();
    }

    @Override
    public List<Section> readAll() {
        return repository.findAll();
    }

    @Override
    public Section save(Section entity) {
        return repository.save(entity);
    }
}
