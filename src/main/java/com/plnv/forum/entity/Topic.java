package com.plnv.forum.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "topics")
public class Topic {
    @Id
    @SequenceGenerator(
            name = "sections_sequence",
            sequenceName = "sections_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sections_sequence"
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Section section;

    @OneToMany
    @JoinColumn(name = "topic_id")
    private List<Message> messages;

    @Column(nullable = false)
    @Size(min = 4, message = "Topic's name cannot be shorter than 4 symbols")
    @Size(max = 50, message = "Topic's name cannot be longer than 50 symbols")
    private String name;

    @Size(max = 100, message = "Description cannot be longer than 100 symbols")
    private String description;

    @Size(max = 10000, message = "Topic's text cannot be longer than 10000 symbols")
    private String text;

    @Size(max = 100, message = "Tags cannot be longer than 100 symbols")
    private String tags;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false)
    private Boolean isVerified;

    @Column(nullable = false)
    private Boolean isPinned;

    @Column(nullable = false)
    private Boolean isSecured;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean isClosed;

    @Column(nullable = false)
    private Boolean isHidden;

    @Column(nullable = false)
    private Boolean isDeleted;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    private LocalDateTime changedAt;

    @JsonGetter("section")
    public Long getSection() {
        return section.getId();
    }

    @JsonGetter("messages")
    public Integer getMessages() {
        return messages.size();
    }
}
