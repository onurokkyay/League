package com.krawenn.lol.dto;

import lombok.Data;

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

    private static final String ICON_URL = "https://ddragon.leagueoflegends.com/cdn/14.10.1/img/champion/%s.png";
    private static final String SPLASH_URL = "https://ddragon.leagueoflegends.com/cdn/img/champion/splash/%s_0.jpg";
    private static final String LOADING_URL = "https://ddragon.leagueoflegends.com/cdn/img/champion/loading/%s_0.jpg";

    public void setChampionName(String championName) {
        this.championName = championName;
        this.images = new Images(
            String.format(ICON_URL, championName),
            String.format(SPLASH_URL, championName),
            String.format(LOADING_URL, championName)
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