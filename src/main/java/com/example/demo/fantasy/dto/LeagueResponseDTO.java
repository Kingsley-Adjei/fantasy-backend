package com.example.demo.fantasy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter @Setter @Builder
public class LeagueResponseDTO {
    private Long id;
    private String name;
    private String joinCode;
    private List<TeamStandingDTO> standings; // Sorted list
}