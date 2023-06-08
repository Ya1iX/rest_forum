package com.plnv.forum.service.implementation;

import com.plnv.forum.entity.User;
import com.plnv.forum.model.Role;
import com.plnv.forum.repository.UserRepository;
import com.plnv.forum.service.UserService;
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
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<User> readAll(User entity, Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.name()));

        if (!isAdmin) {
            entity.setIsDeleted(false);
        }

        return repository.findAll(Example.of(entity), pageable).toList();
    }

    @Override
    public List<User> readAllDeleted(Pageable pageable) {
        return repository.findAllByIsDeleted(true, pageable);
    }

    @Override
    public User edit(User entity, String username) {
        User user = repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found by username: " + username));
        if (user.getIsDeleted()) {
            throw new UsernameNotFoundException("User not found by username: " + username);
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.name()));
        boolean isModer = auth.getAuthorities().contains(new SimpleGrantedAuthority(Role.MODER.name()));

        if (!user.getUsername().equals(auth.getName()) & (!isAdmin & !isModer)) {
            throw new AccessDeniedException("You have not permission to edit this user");
        }

        if (isAdmin) {
            user.setRole(entity.getRole() == null ? user.getRole() : entity.getRole());
            user.setIsVerified(entity.getIsVerified() == null ? user.getIsVerified() : entity.getIsVerified());
        }

        user.setUsername(entity.getUsername() == null ? user.getUsername() : entity.getUsername());
        user.setEmail(entity.getEmail() == null ? user.getEmail() : entity.getEmail());
        user.setFirstName(entity.getFirstName() == null ? user.getFirstName() : entity.getFirstName());
        user.setLastName(entity.getLastName() == null ? user.getLastName() : entity.getLastName());
        user.setPassword(entity.getPassword() == null ? user.getPassword() : passwordEncoder.encode(entity.getPassword()));
        user.setAvatarURL(entity.getAvatarURL() == null ? user.getAvatarURL() : entity.getAvatarURL());
        return repository.save(user);
    }

    @Override
    public User setIsLockedByUsername(String username, Boolean isLocked, LocalDateTime lockExpiration) {
        User user = repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User nod found by username: " + username));

        if (user.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException("Impossible to lock admin");
        }

        user.setUserIsLocked(isLocked, lockExpiration);
        return repository.save(user);
    }

    @Override
    public User setIsDeletedByUsername(String username, Boolean isDeleted) {
        User user = repository.findByUsername(username).orElseThrow(() -> new NoSuchElementException("User not found by username: " + username));

        if (user.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException("Impossible to delete admin");
        }

        user.setUserIsDeleted(isDeleted);
        return repository.save(user);
    }

    @Override
    public User postNew(User entity) {
        return repository.save(User.builder()
                .username(entity.getUsername())
                .email(entity.getEmail())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .password(passwordEncoder.encode(entity.getPassword()))
                .role(entity.getRole())
                .isVerified(entity.getIsVerified() != null && entity.getIsVerified())
                .isDeleted(entity.getIsDeleted() != null && entity.getIsDeleted())
                .isLocked(entity.getIsLocked() != null && entity.getIsLocked())
                .lockExpiration(entity.getIsLocked() ? entity.getLockExpiration() : null)
                .createdAt(LocalDateTime.now())
                .avatarURL(entity.getAvatarURL())
                .build()
        );
    }

    @Override
    public User readById(UUID id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.name()));
        User user = repository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found by id: " + id));

        if (user.getIsDeleted() & !isAdmin) {
            throw new NoSuchElementException("User not found by id: " + id);
        }

        return user;
    }

    @Override
    public User readByUsername(String username) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.name()));
        User user = repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found by username: " + username));

        if (user.getIsDeleted() & !isAdmin) {
            throw new NoSuchElementException("User not found by username: " + username);
        }

        return user;
    }

    @Override
    public void hardDeleteById(UUID id) {
        repository.deleteById(id);
    }
}
