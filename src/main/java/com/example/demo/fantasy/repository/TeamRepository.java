package com.example.demo.fantasy.repository;

import com.example.demo.fantasy.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface TeamRepository extends JpaRepository<Team, UUID> {
    Optional<Team> findByUserId(Long userId);
    boolean existsByTeamName(String teamName);
}