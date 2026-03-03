package com.example.demo.fantasy.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter @Setter
public class LineupRequest {
    private List<Long> pitchIds; // The 11 players in the starting XI
    private List<Long> benchIds; // The 4 players on the bench
}