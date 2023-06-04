package com.plnv.forum.controller;

import com.plnv.forum.service.TechnicalDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/technical")
@RequiredArgsConstructor
public class TechnicalDataController {
    private final TechnicalDataService service;
}
