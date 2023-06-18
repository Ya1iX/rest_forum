package com.plnv.forum.controller;

import com.plnv.forum.model.Response;
import com.plnv.forum.service.Service;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

public abstract class AbstractController<T, ID> {
    public abstract Service<T, ID> getService();

    @GetMapping
    public ResponseEntity<Response> getAll(T entity, Pageable pageable) {
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .data(Map.of("entities", getService().readAll(entity, pageable)))
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getById(@PathVariable ID id, @RequestBody(required = false) T entity) {
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .data(Map.of("entity", getService().readById(id, entity)))
                        .build()
        );
    }

    @GetMapping("/deleted")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllDeleted(T entity, Pageable pageable) {
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .data(Map.of("entities", getService().readAllDeleted(entity, pageable)))
                        .build()
        );
    }

    @GetMapping("/hidden")
    @PreAuthorize("hasAnyAuthority('MODER','ADMIN')")
    public ResponseEntity<Response> getAllHidden(T entity, Pageable pageable) {
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .data(Map.of("entities", getService().readAllHidden(entity, pageable)))
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<Response> post(@RequestBody @Valid T entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.CREATED.value())
                        .httpStatus(HttpStatus.CREATED)
                        .message("Created successfully")
                        .data(Map.of("entity", getService().postNew(entity)))
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> edit(@RequestBody T entity, @PathVariable ID id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .message("Edited successfully")
                        .data(Map.of("entity", getService().edit(entity, id)))
                        .build()
        );
    }

    @PutMapping("/{id}/hide")
    public ResponseEntity<Response> hide(@PathVariable ID id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .message("Hidden successfully")
                        .data(Map.of("entity", getService().setIsHiddenById(id, true)))
                        .build()
        );
    }

    @PutMapping("/{id}/expose")
    public ResponseEntity<Response> expose(@PathVariable ID id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .message("Exposed successfully")
                        .data(Map.of("entity", getService().setIsHiddenById(id, false)))
                        .build()
        );
    }

    @PutMapping("/{id}/restore")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> restore(@PathVariable ID id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .message("Restored successfully")
                        .data(Map.of("entity", getService().setIsDeletedById(id, false)))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> delete(@PathVariable ID id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .message("Deleted successfully")
                        .data(Map.of("entity", getService().setIsDeletedById(id, true)))
                        .build()
        );
    }
}
