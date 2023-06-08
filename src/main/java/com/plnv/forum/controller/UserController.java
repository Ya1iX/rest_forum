package com.plnv.forum.controller;

import com.plnv.forum.entity.User;
import com.plnv.forum.model.Response;
import com.plnv.forum.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping
    public ResponseEntity<Response> getAll(User user, Pageable pageable) {
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .data(Map.of("users", service.readAll(user, pageable)))
                        .build()
        );
    }

    @GetMapping("/{username}")
    public ResponseEntity<Response> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .data(Map.of("user", service.readByUsername(username)))
                        .build()
        );
    }

    @GetMapping("/deleted")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllDeleted(Pageable pageable) {
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .data(Map.of("users", service.readAllDeleted(pageable)))
                        .build()
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> createNewUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.CREATED.value())
                        .httpStatus(HttpStatus.CREATED)
                        .message("Created successfully")
                        .data(Map.of("user", service.postNew(user)))
                        .build()
        );
    }

    @PutMapping("/{username}")
    @PreAuthorize("hasAnyAuthority('USER', 'MODER', 'ADMIN')")
    public ResponseEntity<Response> editUser(@RequestBody User user, @PathVariable String username) {
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .message("Edited successfully")
                        .data(Map.of("user", service.edit(user, username)))
                        .build()
        );
    }

    @PutMapping("/{username}/lock")
    @PreAuthorize("hasAnyAuthority('MODER', 'ADMIN')")
    public ResponseEntity<Response> lockUser(@PathVariable String username, @RequestBody(required = false) LocalDateTime lockExpiration) {
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .message("Locked successfully")
                        .data(Map.of("user", service.setIsLockedByUsername(username, true, lockExpiration)))
                        .build()
        );
    }

    @PutMapping("/{username}/unlock")
    @PreAuthorize("hasAnyAuthority('MODER', 'ADMIN')")
    public ResponseEntity<Response> unlockUser(@PathVariable String username) {
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .message("Unlocked successfully")
                        .data(Map.of("user", service.setIsLockedByUsername(username, false, null)))
                        .build()
        );
    }

    @PutMapping("/{username}/restore")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> restoreUser(@PathVariable String username) {
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .message("Restored successfully")
                        .data(Map.of("user", service.setIsDeletedByUsername(username, false)))
                        .build()
        );
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> softDelete(@PathVariable String username) {
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .message("Deleted successfully")
                        .data(Map.of("user", service.setIsDeletedByUsername(username, false)))
                        .build()
        );
    }

    @DeleteMapping("/{id}/hard-delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> hardDelete(@PathVariable UUID id) {
        service.hardDeleteById(id);
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .message("Removed from database successfully")
                        .build()
        );
    }
}
