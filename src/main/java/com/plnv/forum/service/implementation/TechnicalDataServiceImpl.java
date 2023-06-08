package com.plnv.forum.service.implementation;

import com.plnv.forum.entity.TechnicalData;
import com.plnv.forum.model.Role;
import com.plnv.forum.repository.TechnicalDataRepository;
import com.plnv.forum.service.TechnicalDataService;
import lombok.RequiredArgsConstructor;
import org.hibernate.id.IdentifierGenerationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TechnicalDataServiceImpl implements TechnicalDataService {
    private final TechnicalDataRepository repository;

    @Override
    public List<TechnicalData> readAll(TechnicalData entity, Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.name()));

        if (!isAdmin) {
            entity.setIsPublic(true);
        }

        return repository.findAll(Example.of(entity), pageable).toList();
    }

    @Override
    public List<TechnicalData> readAllDeleted(Pageable pageable) {
        return null;
    }

    @Override
    public TechnicalData edit(TechnicalData entity, String id) {
        TechnicalData data = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Technical data not found by id: " + id));
        data.setText(entity.getText() == null ? data.getText() : entity.getText());
        data.setNotes(entity.getNotes() == null ? data.getNotes() : entity.getNotes());
        data.setIsPublic(entity.getIsPublic() == null ? data.getIsPublic() : entity.getIsPublic());
        return repository.save(data);
    }

    @Override
    public TechnicalData postNew(TechnicalData entity) {
        if (repository.existsById(entity.getId())) {
            throw new IdentifierGenerationException("Data already exists with id: " + entity.getId());
        }
        return repository.save(TechnicalData.builder()
                .id(entity.getId())
                .text(entity.getText())
                .notes(entity.getNotes())
                .isPublic(entity.getIsPublic() != null && entity.getIsPublic())
                .build()
        );
    }

    @Override
    public TechnicalData readById(String id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.name()));
        TechnicalData data = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Technical data not found by id: " + id));

        if (!data.getIsPublic() & !isAdmin) {
            throw new NoSuchElementException("Data not found by id: " + id);
        }

        return data;
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
