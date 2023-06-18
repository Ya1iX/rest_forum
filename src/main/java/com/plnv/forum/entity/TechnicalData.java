package com.plnv.forum.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "technical_data")
public class TechnicalData {
    @Id
    @NotBlank(message = "Data's id cannot be blank")
    private String id;

    @NotBlank(message = "Data's text cannot be blank")
    @Column(nullable = false, columnDefinition = "text")
    private String text;

    private String notes;

    @Column(nullable = false)
    private Boolean isHidden;

    @Column(nullable = false)
    private Boolean isDeleted;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime changedAt;
}
