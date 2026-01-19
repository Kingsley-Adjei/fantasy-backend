package com.example.demo.fantasy.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class TeamRequest {
    private String teamName;
    private List<UUID> playerIds; // The 15 players they selected
}