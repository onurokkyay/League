package com.krawenn.lol.dto;

import lombok.Data;

@Data
public class RewardConfigDto {
    private String rewardValue;
    private String rewardType;
    private int maximumReward;
} 