package com.krawenn.lol.dto;

import lombok.Data;

@Data
public class NextSeasonMilestonesDto {
    private Object requireGradeCounts;
    private int rewardMarks;
    private boolean bonus;
    private RewardConfigDto rewardConfig;
} 