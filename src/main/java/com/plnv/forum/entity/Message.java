package com.plnv.forum.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Topic topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false)
    @Size(max = 3000, message = "Message's text cannot be longer than 3000")
    private String text;

    @Column(nullable = false)
    private Boolean isPinned;

    @Column(nullable = false)
    private Boolean isHidden;

    @Column(nullable = false)
    private Boolean isDeleted;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    private LocalDateTime changedAt;

    @JsonGetter("topic")
    public Long getTopicId() {
        return topic.getId();
    }

    @JsonGetter("user")
    public String getUserName() {
        return user.getUsername();
    }

    public void setMessageIsDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        this.setChangedAt(LocalDateTime.now());
    }

    public void setMessageIsHidden(Boolean isHidden) {
        this.setIsHidden(isHidden);
        this.setChangedAt(LocalDateTime.now());
    }
}
