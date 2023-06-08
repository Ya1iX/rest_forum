package com.plnv.forum.service.implementation;

import com.plnv.forum.entity.Message;
import com.plnv.forum.entity.Topic;
import com.plnv.forum.entity.User;
import com.plnv.forum.model.Role;
import com.plnv.forum.repository.MessageRepository;
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
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<Message> readAll(Message entity, Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.name()));
        boolean isModer = auth.getAuthorities().contains(new SimpleGrantedAuthority(Role.MODER.name()));

        if (!(isAdmin | isModer)) {
            entity.setIsHidden(false);
        }
        if (!isAdmin) {
            entity.setIsDeleted(false);
        }

        return repository.findAll(Example.of(entity), pageable).toList();
    }

    @Override
    public Message readById(UUID id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.name()));
        boolean isModer = auth.getAuthorities().contains(new SimpleGrantedAuthority(Role.MODER.name()));
        Message message = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Message not found by id: " + id));

        if ((message.getIsDeleted() | message.getIsHidden()) & (!isAdmin & !isModer)) {
            throw new NoSuchElementException("Message not found by id: " + id);
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

        if (message.getTopic().getIsSecured() & (!isAdmin & !isModer)) {
            if (entity.getTopic().getPassword() == null) {
                throw new AccessDeniedException("Please, enter the password for topic: " + message.getTopic().getName());
            }
            if (!passwordEncoder.matches(entity.getTopic().getPassword(), message.getTopic().getPassword())) {
                throw new AccessDeniedException("Wrong topic password");
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

        if (topicDB.getIsSecured() & (!isAdmin & !isModer)) {
            if (topic.getPassword() == null) {
                throw new AccessDeniedException("Please, enter the password for topic: " + topicDB.getName());
            }
            if (!passwordEncoder.matches(topic.getPassword(), topicDB.getPassword())) {
                throw new AccessDeniedException("Wrong topic password");
            }
        }

        return repository.save(Message.builder()
                .topic(entity.getTopic())
                .user(user)
                .text(entity.getText())
                .rating(0)
                .isPinned(false)
                .isHidden(false)
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .changedAt(LocalDateTime.now())
                .build()
        );
    }

    @Override
    public void hardDeleteById(UUID id) {
        repository.deleteById(id);
    }
}
