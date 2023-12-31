package com.plnv.forum.service;

import com.plnv.forum.entity.User;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface UserService {
    User readById(UUID id);

    User edit(User entity, String username);

    User setIsDeletedByUsername(String username, Boolean isDeleted);

    User setIsLockedByUsername(String username, Boolean isLocked, LocalDateTime lockExpiration);

    User readByUsername(String username);

    User postNew(User entity);

    List<User> readAllDeleted(User entity, Pageable pageable);

    List<User> readAll(User entity, Pageable pageable);
}
