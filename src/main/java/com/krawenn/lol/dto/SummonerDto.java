package com.krawenn.lol.dto;

import lombok.Data;
import com.krawenn.lol.constants.RiotApiConstants;

@Data
public class SummonerDto {
    private String accountId;
    private int profileIconId;
    private long revisionDate;
    private String id;
    private String puuid;
    private long summonerLevel;
    private String profileIconUrl;

    private static final String PROFILE_ICON_URL_TEMPLATE = RiotApiConstants.DDRAGON_PROFILE_ICON_URL;

    public void setProfileIconId(int profileIconId) {
        this.profileIconId = profileIconId;
        this.profileIconUrl = String.format(PROFILE_ICON_URL_TEMPLATE, profileIconId);
    }
} 