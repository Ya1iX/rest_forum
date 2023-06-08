package com.plnv.forum.controller;

import com.plnv.forum.entity.Section;
import com.plnv.forum.model.Response;
import com.plnv.forum.service.SectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/sections")
@RequiredArgsConstructor
public class SectionController {
    private final SectionService service;

    @GetMapping
    public ResponseEntity<Response> getAll(Section section, Pageable pageable) {
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .data(Map.of("sections", service.readAll(section, pageable)))
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getSectionById(@PathVariable Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .data(Map.of("section", service.readById(id)))
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
                        .data(Map.of("sections", service.readAllDeleted(pageable)))
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
                        .data(Map.of("sections", service.readAllHidden(pageable)))
                        .build()
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> postSection(@RequestBody Section section) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.CREATED.value())
                        .httpStatus(HttpStatus.CREATED)
                        .message("Created successfully")
                        .data(Map.of("section", service.postNew(section)))
                        .build()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> editSection(@RequestBody Section section, @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .message("Edited successfully")
                        .data(Map.of("section", service.edit(section, id)))
                        .build()
        );
    }

    @PutMapping("/{id}/hide")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> hideSection(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .message("Hidden successfully")
                        .data(Map.of("section", service.setIsHiddenById(id, true)))
                        .build()
        );
    }

    @PutMapping("/{id}/restore")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> restoreSection(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .message("Restored successfully")
                        .data(Map.of("section", service.setIsDeletedById(id, false)))
                        .build()
        );
    }

    @PutMapping("/{id}/expose")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> exposeSection(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .message("Exposed successfully")
                        .data(Map.of("section", service.setIsHiddenById(id, false)))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> softDelete(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .message("Deleted successfully")
                        .data(Map.of("section", service.setIsDeletedById(id, true)))
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
