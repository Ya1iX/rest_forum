package com.plnv.forum.service;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface Service<T> {
    List<T> readAll(Pageable pageable);
    List<T> readAllDeleted(Pageable pageable);
    T postNew(T entity);
}
