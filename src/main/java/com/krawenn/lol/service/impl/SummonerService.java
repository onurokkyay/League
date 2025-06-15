package com.krawenn.lol.service.impl;

import com.krawenn.lol.constants.RiotApiConstants;
import com.krawenn.lol.dto.*;
import com.krawenn.lol.enums.Region;
import com.krawenn.lol.service.ISummonerService;
import com.krawenn.lol.util.RiotApiCaller;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SummonerService implements ISummonerService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${riot.api.key}")
    private String riotApiKey;

    @Retry(name = "riotGamesRetry")
    public List<String> getMatchIdsByPuuid(Region region, String puuid, int start, int count) {
        String url = RiotApiConstants.RIOT_MATCH_API_URL.replace("<REGION>", region.getAccountRoute()) + puuid + "/ids?start=" + start + "&count=" + count + "&api_key=" + riotApiKey;
        return RiotApiCaller.execute(() -> {
            ResponseEntity<String[]> response = restTemplate.getForEntity(url, String[].class);
            return Arrays.asList(response.getBody());
        });
    }

    @Retry(name = "riotGamesRetry")
    @Cacheable("matchDetails")
    public MatchDto getMatchDetails(Region region, String matchId) {
        String url = RiotApiConstants.RIOT_MATCH_DETAILS_API_URL.replace("<REGION>", region.getAccountRoute()) + matchId + "?api_key=" + riotApiKey;
        return RiotApiCaller.execute(() -> {
            ResponseEntity<MatchDto> response = restTemplate.getForEntity(url, MatchDto.class);
            return response.getBody();
        });
    }

    @Retry(name = "riotGamesRetry")
    @Cacheable("accountByRiotId")
    public AccountDto getAccountDtoByRiotId(Region region, String gameName, String tagLine) {
        String url = RiotApiConstants.RIOT_ACCOUNT_API_URL.replace("<REGION>", region.getAccountRoute()) + gameName + "/" + tagLine + "?api_key=" + riotApiKey;
        return RiotApiCaller.execute(() -> {
            ResponseEntity<AccountDto> response = restTemplate.getForEntity(url, AccountDto.class);
            return response.getBody();
        });
    }

    @Retry(name = "riotGamesRetry")
    public SummonerDto getSummonerByPuuid(Region region, String puuid) {
        String url = RiotApiConstants.RIOT_SUMMONER_BY_PUUID_API_URL.replace("<REGION>", region.getApiCode()) + puuid + "?api_key=" + riotApiKey;
        return RiotApiCaller.execute(() -> {
            ResponseEntity<SummonerDto> response = restTemplate.getForEntity(url, SummonerDto.class);
            return response.getBody();
        });
    }

    @Retry(name = "riotGamesRetry")
    public List<ChampionMasteryDto> getChampionMasteriesByPuuid(Region region, String puuid) {
        String url = RiotApiConstants.RIOT_CHAMPION_MASTERY_API_URL.replace("<REGION>", region.getApiCode()) + puuid + "?api_key=" + riotApiKey;
        return RiotApiCaller.execute(() -> {
            ResponseEntity<ChampionMasteryDto[]> response = restTemplate.getForEntity(url, ChampionMasteryDto[].class);
            return Arrays.asList(response.getBody());
        });
    }

    @Override
    public List<ChampionDto> getChampionUsage(Region region, String gameName, String tagLine, int count) {
        AccountDto account = getAccountDtoByRiotId(region, gameName, tagLine);
        String puuid = account.getPuuid();
        List<String> matchIds = getMatchIdsByPuuid(region, puuid, 0, count);

        // Flatten all relevant participants
        List<ParticipantDto> participants = matchIds.stream()
            .map(matchId -> getMatchDetails(region, matchId))
            .filter(match -> match != null && match.getInfo() != null && match.getInfo().getParticipants() != null)
            .flatMap(match -> match.getInfo().getParticipants().stream())
            .filter(p -> puuid.equals(p.getPuuid()))
            .toList();

        // Group by championId
        Map<String, List<ParticipantDto>> grouped = participants.stream()
            .collect(Collectors.groupingBy(p -> String.valueOf(p.getChampionId())));

        // Map to ChampionDto and calculate stats
        List<ChampionDto> result = grouped.entrySet().stream()
            .map(entry -> {
                String champId = entry.getKey();
                List<ParticipantDto> plist = entry.getValue();
                int matchCount = plist.size();
                ChampionDto dto = new ChampionDto();
                dto.setChampionId(champId);
                dto.setChampionName(plist.getFirst().getChampionName());
                dto.setCount(matchCount);
                dto.setAvgKills(plist.stream().mapToInt(ParticipantDto::getKills).average().orElse(0));
                dto.setAvgDeaths(plist.stream().mapToInt(ParticipantDto::getDeaths).average().orElse(0));
                dto.setAvgAssists(plist.stream().mapToInt(ParticipantDto::getAssists).average().orElse(0));
                dto.setAvgGold(plist.stream().mapToInt(ParticipantDto::getGoldEarned).average().orElse(0));
                dto.setAvgDamage(plist.stream().mapToInt(ParticipantDto::getTotalDamageDealtToChampions).average().orElse(0));
                dto.setAvgMinions(plist.stream().mapToInt(p -> p.getTotalMinionsKilled() + p.getNeutralMinionsKilled()).average().orElse(0));
                dto.setWinRate(plist.stream().mapToInt(p -> p.isWin() ? 1 : 0).average().orElse(0));
                return dto;
            })
            .sorted((a, b) -> Integer.compare(b.getCount(), a.getCount()))
            .collect(Collectors.toList());

        return result;
    }
} 