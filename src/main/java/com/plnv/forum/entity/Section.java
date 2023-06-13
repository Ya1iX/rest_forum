package com.plnv.forum.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sections")
public class Section {
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

    @OneToMany
    @JoinColumn(name = "section_id")
    private List<Topic> topics;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Section's name cannot be blank")
    @Size(min = 4, message = "Section's name cannot be shorter than 4 symbols")
    @Size(max = 100, message = "Section's name cannot be longer than 100 symbols")
    private String name;

    @Size(max = 300, message = "Description cannot be longer than 300 symbols")
    private String description;

    @Size(max = 100, message = "Tags cannot be longer than 100 symbols")
    private String tags;

    @Column(nullable = false)
    private Boolean isPinned;

    @Column(nullable = false)
    private Boolean isUsersAllowed;

    @Column(nullable = false)
    private Boolean isSecured;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(nullable = false)
    private Boolean isHidden;

    @Column(nullable = false)
    private Boolean isDeleted;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime changedAt;

    @URL
    private String iconURL;

    @JsonGetter("topics")
    public Integer getTopicsSize() {
        return topics == null ? 0 : topics.size();
    }

    public void setSectionIsDeleted(Boolean isDeleted) {
        this.topics.forEach(topic -> topic.setTopicIsDeleted(isDeleted));
        this.setIsDeleted(isDeleted);
        this.setChangedAt(LocalDateTime.now());
    }

    public void setSectionIsHidden(Boolean isHidden) {
        this.topics.forEach(topic -> topic.setTopicIsHidden(isHidden));
        this.setIsHidden(isHidden);
        this.setChangedAt(LocalDateTime.now());
    }
}
