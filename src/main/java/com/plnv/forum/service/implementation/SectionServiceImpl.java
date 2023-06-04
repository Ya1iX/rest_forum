package com.plnv.forum.service.implementation;

import com.plnv.forum.entity.Section;
import com.plnv.forum.model.Role;
import com.plnv.forum.repository.SectionRepository;
import com.plnv.forum.service.SectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService {
    private final SectionRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Section readById(Long id) {
        Section section = repository.findByIdAndIsDeletedAndIsHidden(id, false, false).orElseThrow(() -> new NoSuchElementException("Section not found by id: " + id));
        if (section.getIsDeleted()) {
            throw new NoSuchElementException("Section not found by id: " + id);
        }
        return section;
    }

    @Override
    public List<Section> readAll(
            Long id,
            String name,
            String description,
            String tags,
            Boolean isPinned,
            Boolean isSecured,
            String password,
            Boolean isHidden,
            Boolean isDeleted,
            LocalDateTime createdAt,
            LocalDateTime changedAt,
            String iconURL
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.name()));
        boolean isModer = auth.getAuthorities().contains(new SimpleGrantedAuthority(Role.MODER.name()));

        if (!(isAdmin | isModer)) {
            isHidden = false;
        }
        if (!isAdmin) {
            isDeleted = false;
        }

        Section section = Section.builder()
                .id(id)
                .name(name)
                .description(description)
                .tags(tags)
                .isPinned(isPinned)
                .isSecured(isSecured)
                .password(password)
                .isHidden(isHidden)
                .isDeleted(isDeleted)
                .createdAt(createdAt)
                .changedAt(changedAt)
                .iconURL(iconURL)
                .build();
        return repository.findAll(Example.of(section));
    }

    @Override
    public Section readAnyById(Long id) {
        Section section = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Section not found or was deleted by id: " + id));
        if (section.getIsDeleted()) {
            throw new NoSuchElementException("Section not found by id: " + id);
        }
        return section;
    }

    @Override
    public List<Section> readAll(Pageable pageable) {
        return repository.findAllByIsDeletedAndIsHidden(false, false, pageable);
    }

    @Override
    public List<Section> readAllDeleted(Pageable pageable) {
        return repository.findAllByIsDeleted(true, pageable);
    }

    @Override
    public List<Section> readAllHidden(Pageable pageable) {
        return repository.findAllByIsDeletedAndIsHidden(false, true, pageable);
    }

    @Override
    public Section deleteById(Long id) {
        Section section = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Section not found by id: " + id));
        section.setIsDeleted(true);
        section.setChangedAt(LocalDateTime.now());
        return repository.save(section);
    }

    @Override
    public void hardDeleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Section restoreById(Long id) {
        Section section = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Section not found by id: " + id));
        section.setIsDeleted(false);
        section.setChangedAt(LocalDateTime.now());
        return repository.save(section);
    }

    @Override
    public Section edit(Section entity, Long id) {
        Section section = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Section not found by id: " + id));
        section.setName(entity.getName() == null ? section.getName() : entity.getName());
        section.setDescription(entity.getDescription() == null ? section.getDescription() : entity.getDescription());
        section.setTags(entity.getTags() == null ? section.getTags() : entity.getTags());
        section.setIsPinned(entity.getIsPinned() == null ? section.getIsPinned() : entity.getIsPinned());
        section.setIsDeleted(entity.getIsDeleted() == null ? section.getIsDeleted() : entity.getIsDeleted());
        section.setIsHidden(entity.getIsHidden() == null ? section.getIsHidden() : entity.getIsHidden());
        section.setIsSecured(entity.getIsSecured() == null ? section.getIsSecured() : entity.getIsSecured());
        section.setPassword(entity.getPassword() == null ? section.getPassword() : passwordEncoder.encode(entity.getPassword()));
        section.setChangedAt(LocalDateTime.now());
        section.setIconURL(entity.getIconURL() == null ? section.getIconURL() : entity.getIconURL());
        return repository.save(section);
    }

    @Override
    public Section postNew(Section entity) {
        return repository.save(Section.builder()
                .name(entity.getName())
                .description(entity.getDescription())
                .tags(entity.getTags())
                .isPinned(false)
                .isDeleted(false)
                .isHidden(false)
                .isSecured(entity.getIsSecured())
                .password(entity.getPassword() == null ? null : passwordEncoder.encode(entity.getPassword()))
                .createdAt(LocalDateTime.now())
                .changedAt(LocalDateTime.now())
                .iconURL(entity.getIconURL())
                .build());
    }
}
