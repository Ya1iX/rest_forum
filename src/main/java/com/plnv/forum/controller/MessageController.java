package com.plnv.forum.controller;

import com.plnv.forum.entity.Message;
import com.plnv.forum.model.Response;
import com.plnv.forum.service.MessageService;
import jakarta.validation.Valid;
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
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService service;

    @GetMapping
    public ResponseEntity<Response> getAllMessages(@RequestBody(required = false) Message message, Pageable pageable) {
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .data(Map.of("messages", service.readAll(message, pageable)))
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getMessageById(@PathVariable UUID id, @RequestBody(required = false) Message message) {
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .data(Map.of("message", service.readById(id, message)))
                        .build()
        );
    }

    @GetMapping("/deleted")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllDeleted(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .data(Map.of("messages", service.readAllDeleted(pageable)))
                        .build()
        );
    }

    @GetMapping("/hidden")
    @PreAuthorize("hasAnyAuthority('MODER','ADMIN')")
    public ResponseEntity<Response> getAllHidden(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .data(Map.of("messages", service.readAllHidden(pageable)))
                        .build()
        );
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('USER','MODER','ADMIN')")
    public ResponseEntity<Response> postMessage(@RequestBody @Valid Message message) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.CREATED.value())
                        .httpStatus(HttpStatus.CREATED)
                        .message("Created successfully")
                        .data(Map.of("message", service.postNew(message)))
                        .build()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER','MODER','ADMIN')")
    public ResponseEntity<Response> editMessage(@RequestBody Message message, @PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .message("Edited successfully")
                        .data(Map.of("message", service.edit(message, id)))
                        .build()
        );
    }

    @PutMapping("/{id}/hide")
    @PreAuthorize("hasAnyAuthority('MODER','ADMIN')")
    public ResponseEntity<Response> hideMessage(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .message("Hidden successfully")
                        .data(Map.of("message", service.setIsHiddenById(id, true)))
                        .build()
        );
    }

    @PutMapping("/{id}/expose")
    @PreAuthorize("hasAnyAuthority('MODER','ADMIN')")
    public ResponseEntity<Response> exposeMessage(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .message("Exposed successfully")
                        .data(Map.of("message", service.setIsHiddenById(id, false)))
                        .build()
        );
    }

    @PutMapping("/{id}/restore")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> restoreMessage(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .message("Restored successfully")
                        .data(Map.of("message", service.setIsDeletedById(id, false)))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER','MODER','ADMIN')")
    public ResponseEntity<Response> softDelete(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .message("Deleted successfully")
                        .data(Map.of("message", service.setIsDeletedById(id, true)))
                        .build()
        );
    }

    @DeleteMapping("/{id}/hard-delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> hardDelete(@PathVariable UUID id) {
        service.hardDeleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .message("Removed from database successfully")
                        .build()
        );
    }
}
