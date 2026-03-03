package com.example.demo.fantasy.repository;

import com.example.demo.fantasy.entity.Gameweek;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameweekRepository extends JpaRepository<Gameweek, Integer> {
    // Finds the one gameweek where isCurrent = true
     Gameweek findByIsCurrentTrue();
}