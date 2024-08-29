package com.freeuni.macs.authservice.repository;


import com.freeuni.macs.authservice.model.db.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByUsername(String username);

    void deleteByUsername(String username);
}