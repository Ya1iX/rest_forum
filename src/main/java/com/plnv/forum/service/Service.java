package com.plnv.forum.service;

import java.util.List;

public interface Service<T> {
    List<T> list(int page, int size);
    List<T> readAll();
    T save(T entity);
}
