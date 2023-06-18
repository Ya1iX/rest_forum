package com.plnv.forum.controller;

import com.plnv.forum.entity.Topic;
import com.plnv.forum.model.Response;
import com.plnv.forum.service.Service;
import com.plnv.forum.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/topics")
@RequiredArgsConstructor
public class TopicController extends AbstractController<Topic, Long> {
    private final TopicService service;

    @Override
    public Service<Topic, Long> getService() {
        return service;
    }

    @GetMapping("/{id}/messages")
    public ResponseEntity<Response> getMessages(@PathVariable Long id, @RequestBody(required = false) Topic entity, Pageable pageable) {
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .data(Map.of("entities", service.readMessages(id, entity, pageable)))
                        .build()
        );
    }
}
