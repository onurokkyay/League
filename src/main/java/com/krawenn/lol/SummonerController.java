package com.krawenn.lol;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import java.util.List;
import com.krawenn.lol.dto.MatchDto;
import com.krawenn.lol.dto.AccountDto;
import java.util.Map;
import com.krawenn.lol.dto.ChampionDto;

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
            @PathVariable String tagLine) {
        AccountDto account = summonerService.getAccountDtoByRiotId(region, gameName, tagLine);
        String puuid = account.getPuuid();
        List<String> matchIds = summonerService.getMatchIdsByPuuid(region, puuid, 0, 20);
        Map<String, ChampionDto> championMap = new java.util.HashMap<>();
        for (String matchId : matchIds) {
            com.krawenn.lol.dto.MatchDto match = summonerService.getMatchDetails(region, matchId);
            if (match != null && match.getInfo() != null && match.getInfo().getParticipants() != null) {
                for (com.krawenn.lol.dto.ParticipantDto p : match.getInfo().getParticipants()) {
                    if (puuid.equals(p.getPuuid())) {
                        String champId = String.valueOf(p.getChampionId());
                        ChampionDto dto = championMap.computeIfAbsent(champId, k -> {
                            ChampionDto d = new ChampionDto();
                            d.setChampionId(champId);
                            d.setChampionName(p.getChampionName());
                            return d;
                        });
                        dto.setCount(dto.getCount() + 1);
                        dto.setAvgKills(dto.getAvgKills() + p.getKills());
                        dto.setAvgDeaths(dto.getAvgDeaths() + p.getDeaths());
                        dto.setAvgAssists(dto.getAvgAssists() + p.getAssists());
                        dto.setAvgGold(dto.getAvgGold() + p.getGoldEarned());
                        dto.setAvgDamage(dto.getAvgDamage() + p.getTotalDamageDealtToChampions());
                        dto.setAvgMinions(dto.getAvgMinions() + p.getTotalMinionsKilled() + p.getNeutralMinionsKilled());
                        dto.setWinRate(dto.getWinRate() + (p.isWin() ? 1 : 0));
                    }
                }
            }
        }
        for (ChampionDto dto : championMap.values()) {
            int count = dto.getCount();
            if (count > 0) {
                dto.setAvgKills(dto.getAvgKills() / count);
                dto.setAvgDeaths(dto.getAvgDeaths() / count);
                dto.setAvgAssists(dto.getAvgAssists() / count);
                dto.setAvgGold(dto.getAvgGold() / count);
                dto.setAvgDamage(dto.getAvgDamage() / count);
                dto.setAvgMinions(dto.getAvgMinions() / count);
                dto.setWinRate(dto.getWinRate() / count);
            }
        }
        List<ChampionDto> result = new java.util.ArrayList<>(championMap.values());
        result.sort((a, b) -> Integer.compare(b.getCount(), a.getCount()));
        return ResponseEntity.ok(result);
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
} 