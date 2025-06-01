package com.krawenn.lol.service;

import com.krawenn.lol.enums.Region;
import com.krawenn.lol.dto.*;
import java.util.List;

public interface ISummonerService {
    List<String> getMatchIdsByPuuid(Region region, String puuid, int start, int count);
    MatchDto getMatchDetails(Region region, String matchId);
    AccountDto getAccountDtoByRiotId(Region region, String gameName, String tagLine);
    SummonerDto getSummonerByPuuid(Region region, String puuid);
    List<ChampionMasteryDto> getChampionMasteriesByPuuid(Region region, String puuid);
    List<ChampionDto> getChampionUsage(Region region, String gameName, String tagLine, int count);
} 