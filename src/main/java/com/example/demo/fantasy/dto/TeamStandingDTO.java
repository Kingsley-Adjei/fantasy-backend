package com.example.demo.fantasy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter @Setter @Builder
public class TeamStandingDTO {
    private UUID teamId;
    private String teamName;
    private String managerName;
    private Integer totalPoints;
    private Integer gwPoints;
}