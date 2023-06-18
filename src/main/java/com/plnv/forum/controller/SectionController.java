package com.plnv.forum.controller;

import com.plnv.forum.entity.Section;
import com.plnv.forum.entity.Topic;
import com.plnv.forum.model.Response;
import com.plnv.forum.service.SectionService;
import com.plnv.forum.service.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/sections")
@RequiredArgsConstructor
public class SectionController extends AbstractController<Section, Long> {
    private final SectionService service;

    @Override
    public Service<Section, Long> getService() {
        return service;
    }

    @GetMapping("/{id}/topics")
    public ResponseEntity<Response> getTopics(@PathVariable Long id, @RequestBody(required = false) Section entity, Topic topicEntity, Pageable pageable) {
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .data(Map.of("entities", service.readTopics(id, entity, topicEntity, pageable)))
                        .build()
        );
    }
}
