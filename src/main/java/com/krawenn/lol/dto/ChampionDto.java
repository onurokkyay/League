package com.krawenn.lol.dto;

import lombok.Data;
import com.krawenn.lol.constants.RiotApiConstants;

@Data
public class ChampionDto {
    private String championId;
    private String championName;
    private int count;
    private double avgKills;
    private double avgDeaths;
    private double avgAssists;
    private double avgGold;
    private double avgDamage;
    private double avgMinions;
    private double winRate;

    private Images images;

    public void setChampionName(String championName) {
        this.championName = championName;
        this.images = new Images(
                String.format(RiotApiConstants.DDRAGON_CHAMPION_ICON_URL, championName),
                String.format(RiotApiConstants.DDRAGON_CHAMPION_SPLASH_URL, championName),
                String.format(RiotApiConstants.DDRAGON_CHAMPION_LOADING_URL, championName)
        );
    }

    @Data
    public static class Images {
        private final String icon;
        private final String splash;
        private final String loading;

        public Images(String icon, String splash, String loading) {
            this.icon = icon;
            this.splash = splash;
            this.loading = loading;
        }
    }
} 