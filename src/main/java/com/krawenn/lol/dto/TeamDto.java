package com.krawenn.lol.dto;

import lombok.Data;

import java.util.List;

@Data
public class TeamDto {
    private List<BanDto> bans;
    private Object objectives;
    private int teamId;
    private boolean win;
    // Getters and setters omitted for brevity
} 