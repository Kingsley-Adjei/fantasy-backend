package com.example.demo.fantasy.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Gameweek {
    @Id
    private Integer id; // 1, 2, 3...
    private LocalDateTime deadline;
    private boolean isCurrent;
    private boolean isProcessed; // Have we calculated points yet?
}