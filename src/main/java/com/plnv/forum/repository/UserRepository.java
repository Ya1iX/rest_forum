package com.plnv.forum.repository;

import com.plnv.forum.entity.User;
import com.plnv.forum.model.Role;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    List<User> findByRole(Role role);

    @Modifying
    @Query("UPDATE User u SET u.signedIn = :date WHERE u.username = :username")
    void updateSignedIn(@Param("date") LocalDateTime date, @Param("username") String username);
}
