package com.example.demo.fantasy.repository;

import com.example.demo.fantasy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    // This is the "Magic" method that will help us check
    // if a user exists during your hybrid login flow.
    Optional<User> findByEmail(String email);
}