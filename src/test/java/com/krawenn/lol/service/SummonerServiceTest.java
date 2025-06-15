package com.krawenn.lol.service;

import com.krawenn.lol.dto.*;
import com.krawenn.lol.enums.Region;
import com.krawenn.lol.service.impl.SummonerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SummonerServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private SummonerService summonerService;

    private static final String TEST_API_KEY = "test-api-key";
    private static final String TEST_PUUID = "test-puuid";
    private static final String TEST_GAME_NAME = "testGameName";
    private static final String TEST_TAG_LINE = "TAG";
    private static final String TEST_MATCH_ID = "test-match-id";
    private static final Region TEST_REGION = Region.EUW1;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(summonerService, "riotApiKey", TEST_API_KEY);
    }

    @Test
    @DisplayName("Should return match IDs when getting matches by PUUID")
    void getMatchIdsByPuuid_ShouldReturnMatchIds() {
        // Arrange
        String[] expectedMatchIds = {"match1", "match2"};
        when(restTemplate.getForEntity(anyString(), eq(String[].class)))
                .thenReturn(new ResponseEntity<>(expectedMatchIds, HttpStatus.OK));

        // Act
        List<String> result = summonerService.getMatchIdsByPuuid(TEST_REGION, TEST_PUUID, 0, 20);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("match1", result.get(0));
        assertEquals("match2", result.get(1));
    }

    @Test
    @DisplayName("Should return match details when getting match by ID")
    void getMatchDetails_ShouldReturnMatchDetails() {
        // Arrange
        MatchDto expectedMatch = new MatchDto();
        when(restTemplate.getForEntity(anyString(), eq(MatchDto.class)))
                .thenReturn(new ResponseEntity<>(expectedMatch, HttpStatus.OK));

        // Act
        MatchDto result = summonerService.getMatchDetails(TEST_REGION, TEST_MATCH_ID);

        // Assert
        assertNotNull(result);
        assertEquals(expectedMatch, result);
    }

    @Test
    @DisplayName("Should return account info when getting account by Riot ID")
    void getAccountDtoByRiotId_ShouldReturnAccountInfo() {
        // Arrange
        AccountDto expectedAccount = new AccountDto();
        expectedAccount.setPuuid(TEST_PUUID);
        when(restTemplate.getForEntity(anyString(), eq(AccountDto.class)))
                .thenReturn(new ResponseEntity<>(expectedAccount, HttpStatus.OK));

        // Act
        AccountDto result = summonerService.getAccountDtoByRiotId(TEST_REGION, TEST_GAME_NAME, TEST_TAG_LINE);

        // Assert
        assertNotNull(result);
        assertEquals(TEST_PUUID, result.getPuuid());
    }

    @Test
    @DisplayName("Should return summoner info when getting summoner by PUUID")
    void getSummonerByPuuid_ShouldReturnSummonerInfo() {
        // Arrange
        SummonerDto expectedSummoner = new SummonerDto();
        expectedSummoner.setPuuid(TEST_PUUID);
        when(restTemplate.getForEntity(anyString(), eq(SummonerDto.class)))
                .thenReturn(new ResponseEntity<>(expectedSummoner, HttpStatus.OK));

        // Act
        SummonerDto result = summonerService.getSummonerByPuuid(TEST_REGION, TEST_PUUID);

        // Assert
        assertNotNull(result);
        assertEquals(TEST_PUUID, result.getPuuid());
    }

    @Test
    @DisplayName("Should return champion masteries when getting champion masteries by PUUID")
    void getChampionMasteriesByPuuid_ShouldReturnChampionMasteries() {
        // Arrange
        ChampionMasteryDto[] expectedMasteries = {
            new ChampionMasteryDto(),
            new ChampionMasteryDto()
        };
        when(restTemplate.getForEntity(anyString(), eq(ChampionMasteryDto[].class)))
                .thenReturn(new ResponseEntity<>(expectedMasteries, HttpStatus.OK));

        // Act
        List<ChampionMasteryDto> result = summonerService.getChampionMasteriesByPuuid(TEST_REGION, TEST_PUUID);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Should return champion usage statistics when getting champion usage")
    void getChampionUsage_ShouldReturnChampionUsage() {
        // Arrange
        // Mock account info
        AccountDto accountDto = new AccountDto();
        accountDto.setPuuid(TEST_PUUID);
        when(restTemplate.getForEntity(contains("/riot/account/v1/accounts/by-riot-id/"), eq(AccountDto.class)))
                .thenReturn(new ResponseEntity<>(accountDto, HttpStatus.OK));

        // Mock match IDs
        String[] matchIds = {"match1", "match2"};
        when(restTemplate.getForEntity(contains("/matches/by-puuid/"), eq(String[].class)))
                .thenReturn(new ResponseEntity<>(matchIds, HttpStatus.OK));

        // Mock match details
        MatchDto matchDto = new MatchDto();
        InfoDto infoDto = new InfoDto();
        ParticipantDto participant = new ParticipantDto();
        participant.setPuuid(TEST_PUUID);
        participant.setChampionId(1);
        participant.setChampionName("TestChampion");
        participant.setKills(5);
        participant.setDeaths(2);
        participant.setAssists(10);
        participant.setGoldEarned(10000);
        participant.setTotalDamageDealtToChampions(15000);
        participant.setTotalMinionsKilled(100);
        participant.setNeutralMinionsKilled(20);
        participant.setWin(true);
        infoDto.setParticipants(Arrays.asList(participant));
        matchDto.setInfo(infoDto);
        when(restTemplate.getForEntity(contains("/matches/"), eq(MatchDto.class)))
                .thenReturn(new ResponseEntity<>(matchDto, HttpStatus.OK));

        // Act
        List<ChampionDto> result = summonerService.getChampionUsage(TEST_REGION, TEST_GAME_NAME, TEST_TAG_LINE, 20);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        ChampionDto championDto = result.get(0);
        assertEquals("1", championDto.getChampionId());
        assertEquals("TestChampion", championDto.getChampionName());
        assertEquals(2, championDto.getCount());
        assertEquals(5.0, championDto.getAvgKills());
        assertEquals(2.0, championDto.getAvgDeaths());
        assertEquals(10.0, championDto.getAvgAssists());
        assertEquals(10000.0, championDto.getAvgGold());
        assertEquals(15000.0, championDto.getAvgDamage());
        assertEquals(120.0, championDto.getAvgMinions());
        assertEquals(1.0, championDto.getWinRate());
    }
} 