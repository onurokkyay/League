package com.krawenn.lol;

public class RiotApiConstants {
    public static final String RIOT_ACCOUNT_API_URL = "https://<REGION>.api.riotgames.com/riot/account/v1/accounts/by-riot-id/";
    public static final String RIOT_MATCH_API_URL = "https://<REGION>.api.riotgames.com/lol/match/v5/matches/by-puuid/";
    public static final String RIOT_MATCH_DETAILS_API_URL = "https://<REGION>.api.riotgames.com/lol/match/v5/matches/";
    public static final String RIOT_SUMMONER_BY_PUUID_API_URL = "https://<REGION>.api.riotgames.com/lol/summoner/v4/summoners/by-puuid/";
    public static final String RIOT_CHAMPION_MASTERY_API_URL = "https://<REGION>.api.riotgames.com/lol/champion-mastery/v4/champion-masteries/by-puuid/";
    public static final String DDRAGON_VERSION = "15.11.1";
    public static final String DDRAGON_CHAMPION_ICON_URL = "https://ddragon.leagueoflegends.com/cdn/" + DDRAGON_VERSION + "/img/champion/%s.png";
    public static final String DDRAGON_CHAMPION_SPLASH_URL = "https://ddragon.leagueoflegends.com/cdn/img/champion/splash/%s_0.jpg";
    public static final String DDRAGON_CHAMPION_LOADING_URL = "https://ddragon.leagueoflegends.com/cdn/img/champion/loading/%s_0.jpg";
    public static final String DDRAGON_PROFILE_ICON_URL = "https://ddragon.leagueoflegends.com/cdn/" + DDRAGON_VERSION + "/img/profileicon/%d.png";
} 