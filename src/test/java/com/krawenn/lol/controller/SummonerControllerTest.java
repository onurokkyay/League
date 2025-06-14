package com.krawenn.lol.controller;

import com.krawenn.lol.dto.*;
import com.krawenn.lol.enums.Region;
import com.krawenn.lol.service.impl.SummonerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class SummonerControllerTest {

    @Mock
    private SummonerService summonerService;

    @InjectMocks
    private SummonerController summonerController;

    private static final String TEST_PUUID = "test-puuid";
    private static final String TEST_GAME_NAME = "testGameName";
    private static final String TEST_TAG_LINE = "TAG";
    private static final String TEST_MATCH_ID = "test-match-id";
    private static final Region TEST_REGION = Region.EUW1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getMatchIdsByPuuid_ShouldReturnMatchIds() {
        // Arrange
        List<String> expectedMatchIds = Arrays.asList("match1", "match2");
        when(summonerService.getMatchIdsByPuuid(eq(TEST_REGION), eq(TEST_PUUID), anyInt(), anyInt()))
                .thenReturn(expectedMatchIds);

        // Act
        ResponseEntity<List<String>> response = summonerController.getMatchIdsByPuuid(
                TEST_REGION, TEST_PUUID, 0, 20);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedMatchIds, response.getBody());
    }

    @Test
    void getMatchDetails_ShouldReturnMatchDetails() {
        // Arrange
        MatchDto expectedMatch = new MatchDto();
        when(summonerService.getMatchDetails(eq(TEST_REGION), eq(TEST_MATCH_ID)))
                .thenReturn(expectedMatch);

        // Act
        ResponseEntity<MatchDto> response = summonerController.getMatchDetails(
                TEST_REGION, TEST_MATCH_ID);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedMatch, response.getBody());
    }

    @Test
    void getChampionUsage_ShouldReturnChampionUsage() {
        // Arrange
        List<ChampionDto> expectedChampions = Arrays.asList(new ChampionDto(), new ChampionDto());
        when(summonerService.getChampionUsage(eq(TEST_REGION), eq(TEST_GAME_NAME), eq(TEST_TAG_LINE), anyInt()))
                .thenReturn(expectedChampions);

        // Act
        ResponseEntity<List<ChampionDto>> response = summonerController.getChampionUsage(
                TEST_REGION, TEST_GAME_NAME, TEST_TAG_LINE, 20);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedChampions, response.getBody());
    }

    @Test
    void getSummonerInfo_ShouldReturnSummonerInfo() {
        // Arrange
        AccountDto accountDto = new AccountDto();
        accountDto.setPuuid(TEST_PUUID);
        SummonerDto expectedSummoner = new SummonerDto();
        
        when(summonerService.getAccountDtoByRiotId(eq(TEST_REGION), eq(TEST_GAME_NAME), eq(TEST_TAG_LINE)))
                .thenReturn(accountDto);
        when(summonerService.getSummonerByPuuid(eq(TEST_REGION), eq(TEST_PUUID)))
                .thenReturn(expectedSummoner);

        // Act
        ResponseEntity<SummonerDto> response = summonerController.getSummonerInfo(
                TEST_REGION, TEST_GAME_NAME, TEST_TAG_LINE);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedSummoner, response.getBody());
    }

    @Test
    void getChampionMasteriesByRiotId_ShouldReturnChampionMasteries() {
        // Arrange
        AccountDto accountDto = new AccountDto();
        accountDto.setPuuid(TEST_PUUID);
        List<ChampionMasteryDto> expectedMasteries = Arrays.asList(new ChampionMasteryDto(), new ChampionMasteryDto());
        
        when(summonerService.getAccountDtoByRiotId(eq(TEST_REGION), eq(TEST_GAME_NAME), eq(TEST_TAG_LINE)))
                .thenReturn(accountDto);
        when(summonerService.getChampionMasteriesByPuuid(eq(TEST_REGION), eq(TEST_PUUID)))
                .thenReturn(expectedMasteries);

        // Act
        ResponseEntity<List<ChampionMasteryDto>> response = summonerController.getChampionMasteriesByRiotId(
                TEST_REGION, TEST_GAME_NAME, TEST_TAG_LINE);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedMasteries, response.getBody());
    }
} 