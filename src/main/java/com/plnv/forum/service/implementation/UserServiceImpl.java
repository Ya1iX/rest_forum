package com.plnv.forum.service.implementation;

import com.plnv.forum.entity.User;
import com.plnv.forum.repository.UserRepository;
import com.plnv.forum.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<User> readAll(Pageable pageable) {
        return repository.findAll(pageable).toList();
    }

    @Override
    public List<User> readAllDeleted(Pageable pageable) {
        return null;
    }

    @Override
    public User postNew(User entity) {
        return null;
    }

    @Override
    public User readById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found by id: " + id));
    }

    @Override
    public User readByUsername(String username) {
        return repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found by username: " + username));
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
