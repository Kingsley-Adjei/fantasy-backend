package com.example.demo.fantasy.repository;

import com.example.demo.fantasy.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface TeamRepository extends JpaRepository<Team, UUID> {
}