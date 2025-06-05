package com.krawenn.lol.controller;

import com.krawenn.lol.enums.Region;
import com.krawenn.lol.service.impl.SummonerService;
import com.krawenn.lol.dto.AccountDto;
import com.krawenn.lol.dto.ChampionDto;
import com.krawenn.lol.dto.MatchDto;
import com.krawenn.lol.dto.ChampionMasteryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

@RestController
@RequestMapping("/api/league/summoner")
public class SummonerController {
    @Autowired
    private SummonerService summonerService;

    @Operation(summary = "Get match IDs by PUUID", description = "Returns a list of match IDs for a given player PUUID in the specified region.")
    @GetMapping("/{region}/matches/by-puuid/{puuid}/ids")
    public ResponseEntity<List<String>> getMatchIdsByPuuid(
            @Parameter(description = "Region code, e.g., EUW1, NA1") @PathVariable Region region,
            @Parameter(description = "Player Universal Unique Identifier (PUUID)") @PathVariable String puuid,
            @Parameter(description = "Start index for matches", example = "0") @RequestParam(defaultValue = "0") int start,
            @Parameter(description = "Number of matches to return", example = "20") @RequestParam(defaultValue = "20") int count) {
        return ResponseEntity.ok(summonerService.getMatchIdsByPuuid(region, puuid, start, count));
    }

    @Operation(summary = "Get match details", description = "Returns detailed information for a specific match ID in the given region.")
    @GetMapping("/{region}/matches/{matchId}")
    public ResponseEntity<MatchDto> getMatchDetails(
            @Parameter(description = "Region code, e.g., EUW1, NA1") @PathVariable Region region,
            @Parameter(description = "Match ID") @PathVariable String matchId) {
        return ResponseEntity.ok(summonerService.getMatchDetails(region, matchId));
    }

    @Operation(summary = "Get champion usage statistics", description = "Returns champion usage statistics for a player based on their Riot ID (gameName and tagLine) in the specified region. Optionally specify the number of recent matches to analyze.")
    @GetMapping("/{region}/champion-usage/{gameName}/{tagLine}")
    public ResponseEntity<List<ChampionDto>> getChampionUsage(
            @Parameter(description = "Region code, e.g., EUW1, NA1") @PathVariable Region region,
            @Parameter(description = "Riot game name") @PathVariable String gameName,
            @Parameter(description = "Riot tag line") @PathVariable String tagLine,
            @Parameter(description = "Number of recent matches to analyze", example = "20") @RequestParam(defaultValue = "20") int count) {
        return ResponseEntity.ok(summonerService.getChampionUsage(region, gameName, tagLine, count));
    }

    @Operation(summary = "Get summoner info by Riot ID", description = "Returns detailed summoner information for a player based on their Riot ID (gameName and tagLine) in the specified region.")
    @GetMapping("/{region}/summoner-info/{gameName}/{tagLine}")
    public ResponseEntity<com.krawenn.lol.dto.SummonerDto> getSummonerInfo(
            @Parameter(description = "Region code, e.g., EUW1, NA1") @PathVariable Region region,
            @Parameter(description = "Riot game name") @PathVariable String gameName,
            @Parameter(description = "Riot tag line") @PathVariable String tagLine) {
        AccountDto account = summonerService.getAccountDtoByRiotId(region, gameName, tagLine);
        String puuid = account.getPuuid();
        com.krawenn.lol.dto.SummonerDto summoner = summonerService.getSummonerByPuuid(region, puuid);
        return ResponseEntity.ok(summoner);
    }

    @Operation(summary = "Get champion mastery by Riot ID", description = "Returns champion mastery data for a player based on their Riot ID (gameName and tagLine) in the specified region.")
    @GetMapping("/{region}/champion-mastery/by-riot-id/{gameName}/{tagLine}")
    public ResponseEntity<List<ChampionMasteryDto>> getChampionMasteriesByRiotId(
            @Parameter(description = "Region code, e.g., EUW1, NA1") @PathVariable Region region,
            @Parameter(description = "Riot game name") @PathVariable String gameName,
            @Parameter(description = "Riot tag line") @PathVariable String tagLine) {
        String puuid = summonerService.getAccountDtoByRiotId(region, gameName, tagLine).getPuuid();
        return ResponseEntity.ok(summonerService.getChampionMasteriesByPuuid(region, puuid));
    }
} 