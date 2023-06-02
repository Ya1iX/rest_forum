package com.plnv.forum.repository;

import com.plnv.forum.entity.User;
import com.plnv.forum.model.Role;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findByRole(Role role);

    @Modifying
    @Query("UPDATE User u SET u.signedIn = :date WHERE u.username = :username")
    void updateSignedIn(@Param("date") Date date, @Param("username") String username);
}
