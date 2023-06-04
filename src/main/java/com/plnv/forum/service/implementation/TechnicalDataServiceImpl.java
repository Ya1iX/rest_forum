package com.plnv.forum.service.implementation;

import com.plnv.forum.entity.TechnicalData;
import com.plnv.forum.repository.TechnicalDataRepository;
import com.plnv.forum.service.TechnicalDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TechnicalDataServiceImpl implements TechnicalDataService {
    private TechnicalDataRepository repository;

    @Override
    public List<TechnicalData> list(int page, int size) {
        return repository.findAll(PageRequest.of(page, size)).toList();
    }

    @Override
    public List<TechnicalData> readAll() {
        return repository.findAll();
    }

    @Override
    public TechnicalData save(TechnicalData entity) {
        return repository.save(entity);
    }

    @Override
    public TechnicalData readById(String id) {
        return repository.findById(id).orElseThrow(() -> new NoSuchElementException("Technical data not found by id: " + id));
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
