package com.example.demo.fantasy.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TransferRequest {
    private Long playerOutId; // The ID of the player being sold
    private Long playerInId;  // The ID of the player being bought
}