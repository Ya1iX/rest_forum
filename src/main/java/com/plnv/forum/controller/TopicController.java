package com.plnv.forum.controller;

import com.plnv.forum.entity.Topic;
import com.plnv.forum.model.Response;
import com.plnv.forum.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/topics")
@RequiredArgsConstructor
public class TopicController {
    private final TopicService service;

    @GetMapping
    public ResponseEntity<Response> getAll(Topic topic, Pageable pageable) {
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .data(Map.of("topics", service.readAll(topic, pageable)))
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getTopicById(@PathVariable Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .data(Map.of("topic", service.readById(id)))
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
                        .data(Map.of("topics", service.readAllDeleted(pageable)))
                        .build()
        );
    }

    @GetMapping("/hidden")
    @PreAuthorize("hasAnyAuthority('MODER','ADMIN')")
    public ResponseEntity<Response> getAllHidden(Pageable pageable) {
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .data(Map.of("topics", service.readAllHidden(pageable)))
                        .build()
        );
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('USER','MODER','ADMIN')")
    public ResponseEntity<Response> postTopic(@RequestBody Topic topic) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.CREATED.value())
                        .httpStatus(HttpStatus.CREATED)
                        .message("Created successfully")
                        .data(Map.of("topic", service.postNew(topic)))
                        .build()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER','MODER','ADMIN')")
    public ResponseEntity<Response> editSection(@RequestBody Topic topic, @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .message("Edited successfully")
                        .data(Map.of("topic", service.edit(topic, id)))
                        .build()
        );
    }

    @PutMapping("/{id}/hide")
    @PreAuthorize("hasAnyAuthority('MODER','ADMIN')")
    public ResponseEntity<Response> hideTopic(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .message("Hidden successfully")
                        .data(Map.of("topic", service.setIsHiddenById(id, true)))
                        .build()
        );
    }

    @PutMapping("/{id}/expose")
    @PreAuthorize("hasAnyAuthority('MODER','ADMIN')")
    public ResponseEntity<Response> exposeTopic(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .message("Exposed successfully")
                        .data(Map.of("topic", service.setIsHiddenById(id, false)))
                        .build()
        );
    }

    @PutMapping("/{id}/restore")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> restoreTopic(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .message("Restored successfully")
                        .data(Map.of("topic", service.setIsDeletedById(id, false)))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MODER','ADMIN')")
    public ResponseEntity<Response> softDelete(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .message("Deleted successfully")
                        .data(Map.of("topic", service.setIsDeletedById(id, true)))
                        .build()
        );
    }

    @DeleteMapping("/{id}/hard-delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> hardDelete(@PathVariable Long id) {
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
