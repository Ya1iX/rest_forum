package com.plnv.forum.service.implementation;

import com.plnv.forum.entity.TechnicalData;
import com.plnv.forum.repository.TechnicalDataRepository;
import com.plnv.forum.service.TechnicalDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TechnicalDataServiceImpl implements TechnicalDataService {
    private final TechnicalDataRepository repository;

    @Override
    public List<TechnicalData> readAll(Pageable pageable) {
        return repository.findAll(pageable).toList();
    }

    @Override
    public List<TechnicalData> readAllDeleted(Pageable pageable) {
        return null;
    }

    @Override
    public TechnicalData postNew(TechnicalData entity) {
        return null;
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
