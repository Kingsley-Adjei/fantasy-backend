package com.example.demo.fantasy.repository;

import com.example.demo.fantasy.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlayerRepository extends JpaRepository<Player, UUID> {

    // Find players by position (GK, DEF, MID, FWD) for your frontend filters
    List<Player> findByPosition(String position);

    // Find players by their real-world club
    List<Player> findByRealClub(String realClub);

    // Find players cheaper than a certain price (useful for your budget logic)
    List<Player> findByPriceLessThanEqual(Double price);

    // Search for a player by name (for a search bar)
    List<Player> findByNameContainingIgnoreCase(String name);
}