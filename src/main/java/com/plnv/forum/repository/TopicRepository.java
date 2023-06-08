package com.plnv.forum.repository;

import com.plnv.forum.entity.Topic;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    List<Topic> findAllByIsDeletedAndIsHidden(Boolean isDeleted, Boolean isHidden, Pageable pageable);
    List<Topic> findAllByIsDeleted(Boolean isDeleted, Pageable pageable);
}
