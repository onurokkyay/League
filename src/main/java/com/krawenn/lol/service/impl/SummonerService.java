package com.krawenn.lol.service.impl;

import com.krawenn.lol.enums.Region;
import com.krawenn.lol.constants.RiotApiConstants;
import com.krawenn.lol.dto.AccountDto;
import com.krawenn.lol.dto.MatchDto;
import com.krawenn.lol.dto.SummonerDto;
import com.krawenn.lol.dto.ChampionMasteryDto;
import com.krawenn.lol.service.ISummonerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class SummonerService implements ISummonerService {
    @Value("${riot.api.key}")
    private String riotApiKey;

    public List<String> getMatchIdsByPuuid(Region region, String puuid, int start, int count) {
        RestTemplate restTemplate = new RestTemplate();
        String url = RiotApiConstants.RIOT_MATCH_API_URL.replace("<REGION>", region.getAccountRoute()) + puuid + "/ids?start=" + start + "&count=" + count + "&api_key=" + riotApiKey;
        ResponseEntity<String[]> response = restTemplate.getForEntity(url, String[].class);
        return Arrays.asList(response.getBody());
    }

    @Cacheable("matchDetails")
    public MatchDto getMatchDetails(Region region, String matchId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = RiotApiConstants.RIOT_MATCH_DETAILS_API_URL.replace("<REGION>", region.getAccountRoute()) + matchId + "?api_key=" + riotApiKey;
        ResponseEntity<MatchDto> response = restTemplate.getForEntity(url, MatchDto.class);
        return response.getBody();
    }

    @Cacheable("accountByRiotId")
    public AccountDto getAccountDtoByRiotId(Region region, String gameName, String tagLine) {
        RestTemplate restTemplate = new RestTemplate();
        String url = RiotApiConstants.RIOT_ACCOUNT_API_URL.replace("<REGION>", region.getAccountRoute()) + gameName + "/" + tagLine + "?api_key=" + riotApiKey;
        ResponseEntity<AccountDto> response = restTemplate.getForEntity(url, AccountDto.class);
        return response.getBody();
    }

    public SummonerDto getSummonerByPuuid(Region region, String puuid) {
        RestTemplate restTemplate = new RestTemplate();
        String url = RiotApiConstants.RIOT_SUMMONER_BY_PUUID_API_URL.replace("<REGION>", region.getApiCode()) + puuid + "?api_key=" + riotApiKey;
        ResponseEntity<SummonerDto> response = restTemplate.getForEntity(url, SummonerDto.class);
        return response.getBody();
    }

    public List<ChampionMasteryDto> getChampionMasteriesByPuuid(Region region, String puuid) {
        RestTemplate restTemplate = new RestTemplate();
        String url = RiotApiConstants.RIOT_CHAMPION_MASTERY_API_URL.replace("<REGION>", region.getApiCode()) + puuid + "?api_key=" + riotApiKey;
        ResponseEntity<ChampionMasteryDto[]> response = restTemplate.getForEntity(url, ChampionMasteryDto[].class);
        return Arrays.asList(response.getBody());
    }
} 