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

import java.util.List;

@RestController
@RequestMapping("/summoner")
public class SummonerController {
    @Autowired
    private SummonerService summonerService;

    @GetMapping("/{region}/matches/by-puuid/{puuid}/ids")
    public ResponseEntity<List<String>> getMatchIdsByPuuid(
            @PathVariable Region region,
            @PathVariable String puuid,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "20") int count) {
        return ResponseEntity.ok(summonerService.getMatchIdsByPuuid(region, puuid, start, count));
    }

    @GetMapping("/{region}/matches/{matchId}")
    public ResponseEntity<MatchDto> getMatchDetails(@PathVariable Region region, @PathVariable String matchId) {
        return ResponseEntity.ok(summonerService.getMatchDetails(region, matchId));
    }

    @GetMapping("/{region}/champion-usage/{gameName}/{tagLine}")
    public ResponseEntity<List<ChampionDto>> getChampionUsage(
            @PathVariable Region region,
            @PathVariable String gameName,
            @PathVariable String tagLine,
            @RequestParam(defaultValue = "20") int count) {
        return ResponseEntity.ok(summonerService.getChampionUsage(region, gameName, tagLine, count));
    }

    @GetMapping("/{region}/summoner-info/{gameName}/{tagLine}")
    public ResponseEntity<com.krawenn.lol.dto.SummonerDto> getSummonerInfo(
            @PathVariable Region region,
            @PathVariable String gameName,
            @PathVariable String tagLine) {
        AccountDto account = summonerService.getAccountDtoByRiotId(region, gameName, tagLine);
        String puuid = account.getPuuid();
        com.krawenn.lol.dto.SummonerDto summoner = summonerService.getSummonerByPuuid(region, puuid);
        return ResponseEntity.ok(summoner);
    }

    @GetMapping("/{region}/champion-mastery/by-riot-id/{gameName}/{tagLine}")
    public ResponseEntity<List<ChampionMasteryDto>> getChampionMasteriesByRiotId(
            @PathVariable Region region,
            @PathVariable String gameName,
            @PathVariable String tagLine) {
        String puuid = summonerService.getAccountDtoByRiotId(region, gameName, tagLine).getPuuid();
        return ResponseEntity.ok(summonerService.getChampionMasteriesByPuuid(region, puuid));
    }
} 