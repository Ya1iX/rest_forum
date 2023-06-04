package com.plnv.forum.repository;

import com.plnv.forum.entity.TechnicalData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechnicalDataRepository extends JpaRepository<TechnicalData, String> {
}
