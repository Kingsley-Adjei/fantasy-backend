package com.example.demo.fantasy.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter @Setter @Builder @Data
public class LeagueResponseDTO {
    private Long id;
    private String name;
    private String joinCode;
    private List<TeamStandingDTO> standings;
    private boolean isAdmin;
}