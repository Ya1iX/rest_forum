package com.plnv.forum.service;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface Service<T, ID> {
    List<T> readAll(T entity, Pageable pageable);

    List<T> readAllDeleted(T entity, Pageable pageable);

    T readById(ID id, T entity);

    T edit(T entity, ID id);

    T setIsDeletedById(ID id, Boolean isDeleted);

    T setIsHiddenById(ID id, Boolean isHidden);

    List<T> readAllHidden(T entity, Pageable pageable);

    T postNew(T entity);
}
