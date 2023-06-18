package com.plnv.forum.service.implementation;

import com.plnv.forum.entity.Section;
import com.plnv.forum.entity.Topic;
import com.plnv.forum.model.Role;
import com.plnv.forum.repository.SectionRepository;
import com.plnv.forum.repository.TopicRepository;
import com.plnv.forum.service.SectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final TopicRepository topicRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<Topic> readTopics(Long id, Section entity, Topic topicEntity, Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.name()));
        boolean isModer = auth.getAuthorities().contains(new SimpleGrantedAuthority(Role.MODER.name()));
        Section section = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Section not found by id: " + id));

        if (section.getIsHidden() & (!isAdmin & !isModer)) {
            throw new NoSuchElementException("Section not found by id: " + id);
        }
        if (section.getIsDeleted() & !isAdmin) {
            throw new NoSuchElementException("Section not found by id: " + id);
        }

        if (section.getIsSecured() && !(isAdmin || isModer)) {
            if (entity != null && entity.getPassword() != null) {
                if (!passwordEncoder.matches(entity.getPassword(), section.getPassword())) {
                    throw new AccessDeniedException("Wrong section's password: " + section.getName());
                }
            } else {
                throw new AccessDeniedException("Section requires password: " + section.getName());
            }
        }

        if (!isAdmin && !isModer) {
            topicEntity.setIsHidden(false);
        }
        if (!isAdmin) {
            topicEntity.setIsDeleted(false);
        }

        topicEntity.setSection(section);
        return topicRepository.findAll(Example.of(topicEntity), pageable).toList();
    }

    @Override
    public Section readById(Long id, Section entity) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.name()));
        boolean isModer = auth.getAuthorities().contains(new SimpleGrantedAuthority(Role.MODER.name()));
        Section section = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Section not found by id: " + id));

        if (section.getIsHidden() & (!isAdmin & !isModer)) {
            throw new NoSuchElementException("Section not found by id: " + id);
        }
        if (section.getIsDeleted() & !isAdmin) {
            throw new NoSuchElementException("Section not found by id: " + id);
        }

        return section;
    }

    @Override
    public List<Section> readAll(Section entity, Pageable pageable) {
        if (entity == null) {
            entity = Section.builder().build();
        }

        entity.setIsHidden(false);
        entity.setIsDeleted(false);
        return repository.findAll(Example.of(entity), pageable).toList();
    }

    @Override
    public List<Section> readAllDeleted(Section entity, Pageable pageable) {
        entity.setIsDeleted(true);
        return repository.findAll(Example.of(entity), pageable).toList();
    }

    @Override
    public List<Section> readAllHidden(Section entity, Pageable pageable) {
        entity.setIsHidden(true);
        entity.setIsDeleted(false);
        return repository.findAll(Example.of(entity), pageable).toList();
    }



    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public Section setIsDeletedById(Long id, Boolean isDeleted) {
        Section section = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Section not found by id: " + id));
        section.setSectionIsDeleted(isDeleted);
        section.setChangedAt(LocalDateTime.now());
        return repository.save(section);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public Section setIsHiddenById(Long id, Boolean isHidden) {
        Section section = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Section not found by id: " + id));
        section.setSectionIsHidden(isHidden);
        section.setChangedAt(LocalDateTime.now());
        return repository.save(section);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public Section edit(Section entity, Long id) {
        Section section = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Section not found by id: " + id));
        section.setName(entity.getName() == null ? section.getName() : entity.getName());
        section.setDescription(entity.getDescription() == null ? section.getDescription() : entity.getDescription());
        section.setTags(entity.getTags() == null ? section.getTags() : entity.getTags());
        section.setIsPinned(entity.getIsPinned() == null ? section.getIsPinned() : entity.getIsPinned());
        section.setIsUsersAllowed(entity.getIsUsersAllowed() == null ? section.getIsUsersAllowed() : entity.getIsUsersAllowed());
        section.setIsSecured(entity.getPassword() == null ? section.getIsSecured() : !entity.getPassword().isBlank());
        section.setPassword(entity.getPassword() == null ? section.getPassword() : (entity.getPassword().isBlank() ? null : passwordEncoder.encode(entity.getPassword())));
        section.setChangedAt(LocalDateTime.now());
        section.setIconURL(entity.getIconURL() == null ? section.getIconURL() : entity.getIconURL());
        return repository.save(section);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public Section postNew(Section entity) {
        return repository.save(Section.builder()
                .name(entity.getName())
                .description(entity.getDescription())
                .tags(entity.getTags())
                .isPinned(false)
                .isUsersAllowed(true)
                .isDeleted(false)
                .isHidden(false)
                .isSecured(entity.getPassword() != null && !entity.getPassword().isBlank())
                .password(entity.getPassword() == null ? null : (entity.getPassword().isBlank() ? null : passwordEncoder.encode(entity.getPassword())))
                .createdAt(LocalDateTime.now())
                .iconURL(entity.getIconURL())
                .build());
    }
}
