package com.plnv.forum.auth;

import com.plnv.forum.config.JwtService;
import com.plnv.forum.entity.User;
import com.plnv.forum.model.LoginRequest;
import com.plnv.forum.model.RegisterRequest;
import com.plnv.forum.model.Response;
import com.plnv.forum.model.Role;
import com.plnv.forum.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public Response register(RegisterRequest request, Role role) {
        User user = User.builder()
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .isLocked(false)
                .createdAt(LocalDateTime.now())
                .build();
        repository.save(user);
        String jwt = jwtService.generateToken(user);
        return Response.builder()
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .data(Map.of("token", jwt))
                .build();
    }

    public Response login(LoginRequest request) {
        User user = repository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user = user.isAccountNonLocked() ? user : checkLockExpiration(user);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        String jwt = jwtService.generateToken(user);
        repository.updateSignedIn(LocalDateTime.now(), request.getUsername());
        return Response.builder()
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .data(Map.of("token", jwt))
                .build();
    }

    public Response makeFirstAdmin(RegisterRequest request) {
        if (repository.findByRole(Role.ADMIN).isEmpty()) {
            return register(request, Role.ADMIN);
        }
        throw new NoSuchElementException("Page not found");
    }

    private User checkLockExpiration(User user) {
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime lockExpiration = user.getLockExpiration();
        if (lockExpiration == null) {
            throw new LockedException("Account is permanently locked");
        }
        if (lockExpiration.isBefore(currentDate)) {
            user.setLockExpiration(null);
            user.setIsLocked(false);
            repository.save(user);
            return user;
        }
        throw new LockedException("Account is temporarily locked by " + lockExpiration);
    }
}
