package com.plnv.forum.service.implementation;

import com.plnv.forum.entity.Message;
import com.plnv.forum.entity.Section;
import com.plnv.forum.entity.Topic;
import com.plnv.forum.entity.User;
import com.plnv.forum.model.Role;
import com.plnv.forum.repository.MessageRepository;
import com.plnv.forum.repository.SectionRepository;
import com.plnv.forum.repository.TopicRepository;
import com.plnv.forum.repository.UserRepository;
import com.plnv.forum.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {
    private final TopicRepository repository;
    private final UserRepository userRepository;
    private final SectionRepository sectionRepository;
    private final MessageRepository messageRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<Message> readMessages(Long id, Topic entity, Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.name()));
        boolean isModer = auth.getAuthorities().contains(new SimpleGrantedAuthority(Role.MODER.name()));
        Topic topic = readById(id, entity);

        if (topic.getIsHidden() && (!isAdmin && !isModer)) {
            throw new NoSuchElementException("Topic not found by id: " + id);
        }
        if (topic.getIsDeleted() && !isAdmin) {
            throw new NoSuchElementException("Topic not found by id: " + id);
        }

        if (topic.getIsSecured() && !(isAdmin || isModer)) {
            if (entity != null && entity.getPassword() != null) {
                if (!passwordEncoder.matches(entity.getPassword(), topic.getPassword())) {
                    throw new AccessDeniedException("Wrong topic's password: " + topic.getName());
                }
            } else {
                throw new AccessDeniedException("Topic requires password: " + topic.getName());
            }
        }

        Message message = Message.builder().topic(topic).build();
        if (!isAdmin && !isModer) {
            message.setIsHidden(false);
        }
        if (!isAdmin) {
            message.setIsDeleted(false);
        }
        return messageRepository.findAll(Example.of(message), pageable).toList();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('USER','MODER','ADMIN')")
    public Topic edit(Topic entity, Long id) {
        Topic topic = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Section not found by id: " + id));
        if (topic.getIsDeleted()) {
            throw new AccessDeniedException("Impossible to edit deleted topic");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.name()));
        boolean isModer = auth.getAuthorities().contains(new SimpleGrantedAuthority(Role.MODER.name()));

        if ((!topic.getUserName().equals(auth.getName()) || topic.getIsHidden()) & (!isAdmin & !isModer)) {
            throw new AccessDeniedException("You have not permission to edit this topic");
        }

        if (topic.getSection().getIsSecured() & (!isAdmin & !isModer)) {
            if (entity.getSection() != null && entity.getSection().getPassword() != null) {
                if (!passwordEncoder.matches(entity.getSection().getPassword(), topic.getSection().getPassword())) {
                    throw new AccessDeniedException("Wrong section password");
                }
            } else {
                throw new AccessDeniedException("Section requires password: " + topic.getSection().getName());
            }

        }

        if (isAdmin | isModer) {
            topic.setIsPinned(entity.getIsPinned() == null ? topic.getIsPinned() : entity.getIsPinned());
            topic.setIsClosed(entity.getIsClosed() == null ? topic.getIsClosed() : entity.getIsClosed());
        }
        if (isAdmin) {
            topic.setIsVerified(entity.getIsVerified() == null ? topic.getIsVerified() : entity.getIsVerified());
        }
        topic.setName(entity.getName() == null ? topic.getName() : entity.getName());
        topic.setDescription(entity.getDescription() == null ? topic.getDescription() : entity.getDescription());
        topic.setText(entity.getText() == null ? topic.getText() : entity.getText());
        topic.setTags(entity.getTags() == null ? topic.getTags() : entity.getTags());
        topic.setIsSecured(entity.getPassword() == null ? topic.getIsSecured() : !entity.getPassword().isBlank());
        topic.setPassword(entity.getPassword() == null ? topic.getPassword() : (entity.getPassword().isBlank() ? null : passwordEncoder.encode(entity.getPassword())));
        topic.setChangedAt(LocalDateTime.now());
        return repository.save(topic);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('MODER','ADMIN')")
    public Topic setIsHiddenById(Long id, Boolean isHidden) {
        Topic topic = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Topic not found by id: " + id));
        topic.setTopicIsHidden(isHidden);
        topic.setChangedAt(LocalDateTime.now());
        return repository.save(topic);
    }

    @Override
    public List<Topic> readAll(Topic entity, Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.name()));
        boolean isModer = auth.getAuthorities().contains(new SimpleGrantedAuthority(Role.MODER.name()));

        if (entity == null) {
            entity = Topic.builder().build();
        }
        // Если не админ или модер
        if (!(isAdmin | isModer)) {
            // Выводить топики только из незапароленных разделов
            if (entity.getSection() == null) {
                entity.setSection(Section.builder().isSecured(false).build());
            } else {
                entity.getSection().setIsSecured(false);
            }
        }

        entity.setIsHidden(false);
        entity.setIsDeleted(false);

        return repository.findAll(Example.of(entity), pageable).toList();
    }

    @Override
    public List<Topic> readAllHidden(Topic entity, Pageable pageable) {
        entity.setIsHidden(true);
        entity.setIsDeleted(false);
        return repository.findAll(Example.of(entity), pageable).toList();
    }

    @Override
    public List<Topic> readAllDeleted(Topic entity, Pageable pageable) {
        entity.setIsDeleted(true);
        return repository.findAll(Example.of(entity), pageable).toList();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('USER','MODER','ADMIN')")
    public Topic postNew(Topic entity) {
        Section section = entity.getSection();

        if (section == null) {
            throw new IllegalStateException("Please specify the section where you want to create topic");
        }

        Section sectionDB = sectionRepository.findById(section.getId()).orElseThrow(() -> new NoSuchElementException("Section not found by id: " + section.getId()));

        if (sectionDB.getIsDeleted()) {
            throw new AccessDeniedException("Impossible to create topic in deleted section");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found by username: " + auth.getName()));
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.name()));
        boolean isModer = auth.getAuthorities().contains(new SimpleGrantedAuthority(Role.MODER.name()));

        if ((!sectionDB.getIsUsersAllowed() | sectionDB.getIsHidden()) & (!isAdmin & !isModer)) {
            throw new AccessDeniedException("You have not permission to create topic in section: " + sectionDB.getName());
        }

        if (sectionDB.getIsSecured() & (!isAdmin & !isModer)) {
            if (section.getPassword() == null) {
                throw new AccessDeniedException("Please, enter the password for section: " + sectionDB.getName());
            }
            if (!passwordEncoder.matches(section.getPassword(), sectionDB.getPassword())) {
                throw new AccessDeniedException("Wrong section password");
            }
        }

        return repository.save(Topic.builder()
                .section(entity.getSection())
                .user(user)
                .name(entity.getName())
                .description(entity.getDescription())
                .text(entity.getText())
                .tags(entity.getTags())
                .isVerified(false)
                .isPinned(false)
                .isSecured(entity.getPassword() != null && !entity.getPassword().isBlank())
                .password(entity.getPassword() == null ? null : (entity.getPassword().isBlank() ? null : passwordEncoder.encode(entity.getPassword())))
                .isClosed(false)
                .isHidden(false)
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .build()
        );
    }

    @Override
    public Topic readById(Long id, Topic entity) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.name()));
        boolean isModer = auth.getAuthorities().contains(new SimpleGrantedAuthority(Role.MODER.name()));
        Topic topic = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Topic not found by id: " + id));

        if (topic.getIsHidden() && (!isAdmin && !isModer)) {
            throw new NoSuchElementException("Topic not found by id: " + id);
        }
        if (topic.getIsDeleted() && !isAdmin) {
            throw new NoSuchElementException("Topic not found by id: " + id);
        }
        // Проверка на запароленность раздела
        if (topic.getSection().getIsSecured() & (!isAdmin & !isModer)) {
            if (entity != null && entity.getSection() != null && entity.getSection().getPassword() != null) {
                if (!passwordEncoder.matches(entity.getSection().getPassword(), topic.getSection().getPassword())) {
                    throw new AccessDeniedException("Wrong section's password: " + topic.getSection().getName());
                }
            } else {
                throw new AccessDeniedException("Section requires password: " + topic.getSection().getName());
            }
        }

        return topic;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('MODER','ADMIN')")
    public Topic setIsDeletedById(Long id, Boolean isDeleted) {
        Topic topic = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Topic not found by id: " + id));
        topic.setTopicIsDeleted(isDeleted);
        topic.setChangedAt(LocalDateTime.now());
        return repository.save(topic);
    }
}
