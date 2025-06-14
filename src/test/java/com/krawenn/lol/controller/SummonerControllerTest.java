package com.krawenn.lol.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krawenn.lol.config.MockSummonerServiceTestConfig;
import com.krawenn.lol.dto.*;
import com.krawenn.lol.enums.Region;
import com.krawenn.lol.service.impl.SummonerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SummonerController.class)
@Import(MockSummonerServiceTestConfig.class)
class SummonerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SummonerService summonerService;

    private static final String TEST_PUUID = "test-puuid";
    private static final String TEST_GAME_NAME = "testGameName";
    private static final String TEST_TAG_LINE = "TAG";
    private static final String TEST_MATCH_ID = "test-match-id";
    private static final Region TEST_REGION = Region.EUW1;

    @Test
    void getMatchIdsByPuuid_ShouldReturnMatchIds() throws Exception {
        // Arrange
        List<String> expectedMatchIds = Arrays.asList("match1", "match2");
        when(summonerService.getMatchIdsByPuuid(eq(TEST_REGION), eq(TEST_PUUID), anyInt(), anyInt()))
                .thenReturn(expectedMatchIds);

        // Act & Assert
        mockMvc.perform(get("/api/league/summoner/{region}/matches/by-puuid/{puuid}/ids", TEST_REGION, TEST_PUUID)
                .param("start", "0")
                .param("count", "20")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").value("match1"))
                .andExpect(jsonPath("$[1]").value("match2"));
    }

    @Test
    void getMatchDetails_ShouldReturnMatchDetails() throws Exception {
        // Arrange
        MatchDto expectedMatch = new MatchDto();
        when(summonerService.getMatchDetails(eq(TEST_REGION), eq(TEST_MATCH_ID)))
                .thenReturn(expectedMatch);

        // Act & Assert
        mockMvc.perform(get("/api/league/summoner/{region}/matches/{matchId}", TEST_REGION, TEST_MATCH_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedMatch)));
    }

    @Test
    void getChampionUsage_ShouldReturnChampionUsage() throws Exception {
        // Arrange
        List<ChampionDto> expectedChampions = Arrays.asList(new ChampionDto(), new ChampionDto());
        when(summonerService.getChampionUsage(eq(TEST_REGION), eq(TEST_GAME_NAME), eq(TEST_TAG_LINE), anyInt()))
                .thenReturn(expectedChampions);

        // Act & Assert
        mockMvc.perform(get("/api/league/summoner/{region}/champion-usage/{gameName}/{tagLine}", 
                TEST_REGION, TEST_GAME_NAME, TEST_TAG_LINE)
                .param("count", "20")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedChampions)));
    }

    @Test
    void getSummonerInfo_ShouldReturnSummonerInfo() throws Exception {
        // Arrange
        AccountDto accountDto = new AccountDto();
        accountDto.setPuuid(TEST_PUUID);
        SummonerDto expectedSummoner = new SummonerDto();
        
        when(summonerService.getAccountDtoByRiotId(eq(TEST_REGION), eq(TEST_GAME_NAME), eq(TEST_TAG_LINE)))
                .thenReturn(accountDto);
        when(summonerService.getSummonerByPuuid(eq(TEST_REGION), eq(TEST_PUUID)))
                .thenReturn(expectedSummoner);

        // Act & Assert
        mockMvc.perform(get("/api/league/summoner/{region}/summoner-info/{gameName}/{tagLine}", 
                TEST_REGION, TEST_GAME_NAME, TEST_TAG_LINE)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedSummoner)));
    }

    @Test
    void getChampionMasteriesByRiotId_ShouldReturnChampionMasteries() throws Exception {
        // Arrange
        AccountDto accountDto = new AccountDto();
        accountDto.setPuuid(TEST_PUUID);
        List<ChampionMasteryDto> expectedMasteries = Arrays.asList(new ChampionMasteryDto(), new ChampionMasteryDto());
        
        when(summonerService.getAccountDtoByRiotId(eq(TEST_REGION), eq(TEST_GAME_NAME), eq(TEST_TAG_LINE)))
                .thenReturn(accountDto);
        when(summonerService.getChampionMasteriesByPuuid(eq(TEST_REGION), eq(TEST_PUUID)))
                .thenReturn(expectedMasteries);

        // Act & Assert
        mockMvc.perform(get("/api/league/summoner/{region}/champion-mastery/by-riot-id/{gameName}/{tagLine}", 
                TEST_REGION, TEST_GAME_NAME, TEST_TAG_LINE)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedMasteries)));
    }
}