package com.plnv.forum.controller;

import com.plnv.forum.entity.TechnicalData;
import com.plnv.forum.model.Response;
import com.plnv.forum.service.TechnicalDataService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/technical")
@RequiredArgsConstructor
public class TechnicalDataController {
    private final TechnicalDataService service;

    @GetMapping
    public ResponseEntity<Response> getAllData(TechnicalData data, Pageable pageable) {
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .data(Map.of("technical", service.readAll(data, pageable)))
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getDataById(@PathVariable String id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .data(Map.of("technical", service.readById(id)))
                        .build()
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> postData(@RequestBody @Valid TechnicalData data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.CREATED.value())
                        .httpStatus(HttpStatus.CREATED)
                        .message("Created successfully")
                        .data(Map.of("technical", service.postNew(data)))
                        .build()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> editData(@RequestBody TechnicalData data, @PathVariable String id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .message("Edited successfully")
                        .data(Map.of("technical", service.edit(data, id)))
                        .build()
        );
    }

    @DeleteMapping("/{id}/hard-delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> hardDelete(@PathVariable String id) {
        service.deleteById(id);
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
