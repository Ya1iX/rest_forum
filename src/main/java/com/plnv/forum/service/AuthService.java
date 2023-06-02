package com.plnv.forum.service;

import com.plnv.forum.entity.User;
import com.plnv.forum.model.AuthenticationResponse;
import com.plnv.forum.model.LoginRequest;
import com.plnv.forum.model.RegisterRequest;
import com.plnv.forum.model.Role;
import com.plnv.forum.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountLockedException;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request, Role role) {
        var user = User.builder()
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .isLocked(false)
                .createdAt(new Date(System.currentTimeMillis()))
                .build();
        repository.save(user);
        var jwt = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwt)
                .build();
    }

    public AuthenticationResponse login(LoginRequest request) {
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user = user.isAccountNonLocked() ? user : checkLockExpiration(user);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var jwt = jwtService.generateToken(user);
        repository.updateSignedIn(new Date(System.currentTimeMillis()), request.getUsername());
        return AuthenticationResponse.builder()
                .token(jwt)
                .build();
    }

    public AuthenticationResponse makeFirstAdmin(RegisterRequest request) {
        if (repository.findByRole(Role.ADMIN).isEmpty()) {
            return register(request, Role.ADMIN);
        }
        throw new NoSuchElementException("Page not found");
    }

    private User checkLockExpiration(User user) {
        Date currentDate = new Date(System.currentTimeMillis());
        Date lockExpiration = user.getLockExpiration();
        if (lockExpiration == null) {
            throw new JwtException("ACCESS DENIED: Account is locked permanently");
        }
        if (lockExpiration.before(currentDate)) {
            user.setLockExpiration(null);
            user.setIsLocked(false);
            repository.save(user);
            return user;
        }
        throw new JwtException("ACCESS DENIED: Account is locked by " + lockExpiration);
    }
}
