package com.plnv.forum.service;

import com.plnv.forum.entity.User;

import java.time.LocalDateTime;
import java.util.UUID;

public interface UserService extends Service<User> {
    User readById(UUID id);
    User edit(User entity, String username);
    User setIsDeletedByUsername(String username, Boolean isDeleted);
    User setIsLockedByUsername(String username, Boolean isLocked, LocalDateTime lockExpiration);
    User readByUsername(String username);
    void hardDeleteById(UUID id);
}
