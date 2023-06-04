package com.plnv.forum.service;

import com.plnv.forum.entity.TechnicalData;

public interface TechnicalDataService extends Service<TechnicalData> {
    TechnicalData readById(String id);
    void deleteById(String id);
}
