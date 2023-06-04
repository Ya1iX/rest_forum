package com.plnv.forum.service;

import com.plnv.forum.entity.User;

import java.util.UUID;

public interface UserService extends Service<User> {
    User readById(UUID id);
    User readByUsername(String username);
    void deleteById(UUID id);
}
