package com.plnv.forum.repository;

import com.plnv.forum.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findAllByIsDeletedAndIsHidden(Boolean isDeleted, Boolean isHidden, Pageable pageable);
    List<Message> findAllByIsDeleted(Boolean isDeleted, Pageable pageable);
}
