package com.plnv.forum.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "technical_data")
public class TechnicalData {
    @Id
    @NotNull(message = "Specify the ID for data")
    private String id;

    @Column(nullable = false, columnDefinition = "text")
    private String text;

    private String notes;

    @Column(nullable = false)
    private Boolean isPublic;
}
