package com.plnv.forum.repository;

import com.plnv.forum.entity.Section;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findAllByIsDeletedAndIsHidden(Boolean isDeleted, Boolean isHidden, Pageable pageable);
    List<Section> findAllByIsDeleted(Boolean isDeleted, Pageable pageable);
    Optional<Section> findByIdAndIsDeletedAndIsHidden(Long id, Boolean isDeleted, Boolean isHidden);
}
