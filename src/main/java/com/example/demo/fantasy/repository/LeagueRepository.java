package com.example.demo.fantasy.repository;

import com.example.demo.fantasy.entity.League;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LeagueRepository extends JpaRepository<League, Long> {
    // This query finds all leagues where the specific user's team is a member
    @Query("SELECT l FROM League l JOIN l.members m WHERE m.user.id = :userId")
    List<League> findLeaguesByUserId(@Param("userId") Long userId);

    // Find league by the 6-digit join code
    Optional<League> findByJoinCode(String joinCode);

    // Get all leagues where a specific team is a member
    List<League> findAllByMembersId(UUID teamId);

    boolean existsByName(String name);
}
