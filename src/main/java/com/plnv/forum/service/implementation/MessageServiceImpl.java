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
import com.plnv.forum.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository repository;
    private final TopicRepository topicRepository;
    private final SectionRepository sectionRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<Message> readAll(Message entity, Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.name()));
        boolean isModer = auth.getAuthorities().contains(new SimpleGrantedAuthority(Role.MODER.name()));

        if (entity == null) {
            entity = Message.builder().build();
        }
        if (!(isAdmin | isModer)) {
            entity.setIsHidden(false);

            // Проверка на запароленность топика
            if (entity.getTopic() != null && entity.getTopicId() != null) {
                Topic topic = topicRepository.findById(entity.getTopicId()).orElseThrow(() -> new NoSuchElementException("Topic not found"));
                if (topic.getIsSecured()) {
                    entity.getTopic().setIsSecured(passwordEncoder.matches(entity.getTopic().getPassword(), topic.getPassword()));
                    entity.getTopic().setPassword(null);
                }
            } else if (entity.getTopic() != null) {
                entity.getTopic().setIsSecured(false);
            } else {
                entity.setTopic(Topic.builder().isSecured(false).build());
            }

            // Проверка на запароленность раздела
            if (entity.getTopic() != null && entity.getTopic().getSection() != null && entity.getTopic().getSectionId() != null) {
                Section entitySection = entity.getTopic().getSection();
                Section section = sectionRepository.findById(entitySection.getId()).orElseThrow(() -> new NoSuchElementException("Section not found"));
                if (section.getIsSecured()) {
                    entitySection.setIsSecured(passwordEncoder.matches(entitySection.getPassword(), section.getPassword()));
                    entitySection.setPassword(null);
                    entity.getTopic().setSection(entitySection);
                }
            } else if (entity.getTopic() != null && entity.getTopic().getSection() != null) {
                entity.getTopic().getSection().setIsSecured(false);
            } else {
                entity.getTopic().setSection(Section.builder().isSecured(false).build());
            }
        }
        if (!isAdmin) {
            entity.setIsDeleted(false);
        }

        return repository.findAll(Example.of(entity), pageable).toList();
    }

    @Override
    public Message readById(UUID id, Message entity) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.name()));
        boolean isModer = auth.getAuthorities().contains(new SimpleGrantedAuthority(Role.MODER.name()));
        Message message = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Message not found by id: " + id));

        if ((message.getIsDeleted() | message.getIsHidden()) & (!isAdmin & !isModer)) {
            throw new NoSuchElementException("Message not found by id: " + id);
        }
        // Проверка на запароленность топика
        if (message.getTopic().getIsSecured() & (!isAdmin & !isModer)) {
            if (entity.getTopic() != null && entity.getTopic().getPassword() != null) {
                if (!passwordEncoder.matches(entity.getTopic().getPassword(), message.getTopic().getPassword())) {
                    throw new AccessDeniedException("Wrong topic password");
                }
            } else {
                throw new AccessDeniedException("Topic requires password");
            }
        }
        // Проверка на запароленность раздела
        if (message.getTopic().getSection().getIsSecured() & (!isAdmin & !isModer)) {
            if (entity.getTopic() != null && entity.getTopic().getSection() != null && entity.getTopic().getSection().getPassword() != null) {
                if (!passwordEncoder.matches(entity.getTopic().getSection().getPassword(), message.getTopic().getSection().getPassword())) {
                    throw new AccessDeniedException("Wrong section password");
                }
            } else {
                throw new AccessDeniedException("Section requires password");
            }
        }

        return message;
    }

    @Override
    public List<Message> readAllDeleted(Pageable pageable) {
        return repository.findAllByIsDeleted(true, pageable);
    }

    @Override
    public Message edit(Message entity, UUID id) {
        Message message = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Message not found by id: " + id));
        if (message.getIsDeleted()) {
            throw new AccessDeniedException("Impossible to edit deleted message");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.name()));
        boolean isModer = auth.getAuthorities().contains(new SimpleGrantedAuthority(Role.MODER.name()));

        // Если имя автора не сопадает с именем отправившего запрос,
        // либо сообщение скрыто модерацией,
        // либо прошло больше 24 часов после публикации,
        // либо топик закрыт
        // и автор запроса не является модерацией/администрацией,
        // то запретить редактирование
        if ((!message.getUserName().equals(auth.getName()) |
                message.getIsHidden() |
                !message.getCreatedAt().plusHours(24).isBefore(LocalDateTime.now()) |
                message.getTopic().getIsClosed()) &
                (!isAdmin & !isModer)) {
            throw new AccessDeniedException("You have not permission to edit this message");
        }

        // Проверка на запароленность топика
        if (message.getTopic().getIsSecured() & (!isAdmin & !isModer)) {
            if (entity.getTopic() != null && entity.getTopic().getPassword() != null) {
                if (!passwordEncoder.matches(entity.getTopic().getPassword(), message.getTopic().getPassword())) {
                    throw new AccessDeniedException("Wrong topic password");
                }
            } else {
                throw new AccessDeniedException("Please, enter the password for topic: " + message.getTopic().getName());
            }
        }
        // Проверка на запроленность раздела
        if (message.getTopic().getSection().getIsSecured() & (!isAdmin & !isModer)) {
            if (entity.getTopic() != null && entity.getTopic().getSection() != null && entity.getTopic().getSection().getPassword() != null) {
                if (!passwordEncoder.matches(entity.getTopic().getSection().getPassword(), message.getTopic().getSection().getPassword())) {
                    throw new AccessDeniedException("Wrong section password");
                }
            } else {
                throw new AccessDeniedException("Please, enter the password for section: " + message.getTopic().getSection().getName());
            }
        }

        if (isAdmin | isModer) {
            message.setIsPinned(entity.getIsPinned() == null ? message.getIsPinned() : entity.getIsPinned());
            message.setIsHidden(entity.getIsHidden() == null ? message.getIsHidden() : entity.getIsHidden());
        }
        message.setText(entity.getText() == null ? message.getText() : entity.getText());
        message.setChangedAt(LocalDateTime.now());
        return repository.save(message);
    }

    @Override
    public Message setIsDeletedById(UUID id, Boolean isDeleted) {
        Message message = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Message not found by id: " + id));
        message.setIsDeleted(isDeleted);
        message.setChangedAt(LocalDateTime.now());
        return repository.save(message);
    }

    @Override
    public Message setIsHiddenById(UUID id, Boolean isHidden) {
        Message message = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Message not found by id: " + id));
        message.setIsHidden(isHidden);
        message.setChangedAt(LocalDateTime.now());
        return repository.save(message);
    }

    @Override
    public List<Message> readAllHidden(Pageable pageable) {
        return repository.findAllByIsDeletedAndIsHidden(false, true, pageable);
    }

    @Override
    public Message postNew(Message entity) {
        Topic topic = entity.getTopic();

        if (topic == null) {
            throw new IllegalStateException("Please specify the topic where you want to create message");
        }

        Topic topicDB = topicRepository.findById(topic.getId()).orElseThrow(() -> new NoSuchElementException("Topic not found by id: " + topic.getId()));

        if (topicDB.getIsDeleted()) {
            throw new AccessDeniedException("Impossible to create message in deleted topic");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found by username: " + auth.getName()));
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.name()));
        boolean isModer = auth.getAuthorities().contains(new SimpleGrantedAuthority(Role.MODER.name()));

        if ((topicDB.getIsHidden() | topicDB.getIsClosed()) & (!isAdmin & !isModer)) {
            throw new AccessDeniedException("You have not permission to create message in topic: " + topicDB.getName());
        }

        // Проверка на запароленность топика
        if (topicDB.getIsSecured() & (!isAdmin & !isModer)) {
            if (topic.getPassword() == null) {
                throw new AccessDeniedException("Topic requires password: " + topicDB.getName());
            }
            if (!passwordEncoder.matches(topic.getPassword(), topicDB.getPassword())) {
                throw new AccessDeniedException("Wrong topic password");
            }
        }
        // Проверка на запароленность раздела
        if (topicDB.getSection().getIsSecured() & (!isAdmin & !isModer)) {
            if (topic.getSection() != null && topic.getSection().getPassword() != null) {
                if (!passwordEncoder.matches(topic.getSection().getPassword(), topicDB.getSection().getPassword())) {
                    throw new AccessDeniedException("Wrong section password");
                }
            } else {
                throw new AccessDeniedException("Section requires password: " + topicDB.getSection().getName());
            }
        }

        return repository.save(Message.builder()
                .topic(entity.getTopic())
                .user(user)
                .text(entity.getText())
                .isPinned(false)
                .isHidden(false)
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .build()
        );
    }

    @Override
    public void hardDeleteById(UUID id) {
        repository.deleteById(id);
    }
}
