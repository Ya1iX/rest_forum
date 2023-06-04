package com.plnv.forum.service.implementation;

import com.plnv.forum.entity.User;
import com.plnv.forum.repository.UserRepository;
import com.plnv.forum.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository repository;

    @Override
    public List<User> list(int page, int size) {
        return repository.findAll(PageRequest.of(page, size)).toList();
    }

    @Override
    public List<User> readAll() {
        return repository.findAll();
    }

    @Override
    public User save(User entity) {
        return repository.save(entity);
    }

    @Override
    public User readById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found by id: " + id));
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
