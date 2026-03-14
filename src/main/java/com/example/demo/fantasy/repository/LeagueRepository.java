package com.example.demo.fantasy.repository;

import com.example.demo.fantasy.entity.League;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LeagueRepository extends JpaRepository<League, Long> {
    // Find league by the 6-digit join code
    Optional<League> findByJoinCode(String joinCode);

    Optional<League> findByIsGlobalTrue();

    // Get all leagues where a specific team is a member
    List<League> findAllByMembersId(UUID teamId);

    boolean existsByName(String name);
}
