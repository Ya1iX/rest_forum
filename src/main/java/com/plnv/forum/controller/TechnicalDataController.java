package com.plnv.forum.controller;

import com.plnv.forum.entity.TechnicalData;
import com.plnv.forum.service.Service;
import com.plnv.forum.service.TechnicalDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/technical")
@RequiredArgsConstructor
public class TechnicalDataController extends AbstractController<TechnicalData, String> {
    private final TechnicalDataService service;

    @Override
    public Service<TechnicalData, String> getService() {
        return service;
    }
}
