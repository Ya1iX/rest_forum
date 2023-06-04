package com.plnv.forum.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.plnv.forum.model.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 4, message = "Username cannot be shorter than 4 symbols")
    @Size(max = 20, message = "Username cannot be longer than 20 symbols")
    @Pattern(regexp = "^\\w+", message = "Username can contains only letters, digits and underscores")
    private String username;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @Size(max = 20, message = "First name must be shorter than 20 symbols")
    @Pattern(regexp = "^[a-zA-Z]+", message = "First name can contains only letters [a-z][A-Z]")
    private String firstName;
    @Size(max = 20, message = "Last name must be shorter than 20 symbols")
    @Pattern(regexp = "^[a-zA-Z]+", message = "Last name can contains only letters [a-z][A-Z]")
    private String lastName;

    @Column(nullable = false)
    @NotBlank(message = "Password cannot be blank")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    @NotNull(message = "User's lock status cannot be null")
    private Boolean isLocked;

    @Column(nullable = false)
    @NotNull(message = "Account creation date cannot be null")
    private LocalDateTime createdAt;

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Message> messages;

    private LocalDateTime signedIn;
    private LocalDateTime lockExpiration;
    private String avatarURL;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @JsonGetter("messages")
    public Integer getMessages() {
        return messages.size();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
