package com.plnv.forum.service.implementation;

import com.plnv.forum.entity.TechnicalData;
import com.plnv.forum.model.Role;
import com.plnv.forum.repository.TechnicalDataRepository;
import com.plnv.forum.service.TechnicalDataService;
import lombok.RequiredArgsConstructor;
import org.hibernate.id.IdentifierGenerationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TechnicalDataServiceImpl implements TechnicalDataService {
    private final TechnicalDataRepository repository;

    @Override
    public List<TechnicalData> readAll(TechnicalData entity, Pageable pageable) {
        entity.setIsHidden(false);
        entity.setIsDeleted(false);
        return repository.findAll(Example.of(entity), pageable).toList();
    }

    @Override
    public List<TechnicalData> readAllDeleted(TechnicalData entity, Pageable pageable) {
        entity.setIsDeleted(true);
        return repository.findAll(Example.of(entity), pageable).toList();
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public TechnicalData edit(TechnicalData entity, String id) {
        TechnicalData data = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Technical data not found by id: " + id));
        data.setText(entity.getText() == null ? data.getText() : entity.getText());
        data.setNotes(entity.getNotes() == null ? data.getNotes() : entity.getNotes());
        data.setChangedAt(LocalDateTime.now());
        return repository.save(data);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public TechnicalData setIsDeletedById(String id, Boolean isDeleted) {
        TechnicalData data = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Technical data not found by id: " + id));
        data.setIsDeleted(true);
        data.setChangedAt(LocalDateTime.now());
        return repository.save(data);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public TechnicalData setIsHiddenById(String id, Boolean isHidden) {
        TechnicalData data = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Data not found by id: " + id));
        data.setIsHidden(isHidden);
        data.setChangedAt(LocalDateTime.now());
        return repository.save(data);
    }

    @Override
    public List<TechnicalData> readAllHidden(TechnicalData entity, Pageable pageable) {
        entity.setIsHidden(true);
        entity.setIsDeleted(false);
        return repository.findAll(Example.of(entity), pageable).toList();
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public TechnicalData postNew(TechnicalData entity) {
        if (repository.existsById(entity.getId())) {
            throw new IdentifierGenerationException("Data already exists with id: " + entity.getId());
        }
        return repository.save(TechnicalData.builder()
                .id(entity.getId())
                .text(entity.getText())
                .notes(entity.getNotes())
                .isHidden(entity.getIsHidden() != null && entity.getIsHidden())
                .createdAt(LocalDateTime.now())
                .build()
        );
    }

    @Override
    public TechnicalData readById(String id, TechnicalData entity) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.name()));
        boolean isModer = auth.getAuthorities().contains(new SimpleGrantedAuthority(Role.MODER.name()));
        TechnicalData data = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Technical data not found by id: " + id));

        if (data.getIsHidden() && !(isAdmin || isModer)) {
            throw new NoSuchElementException("Data not found by id: " + id);
        }

        return data;
    }
}
