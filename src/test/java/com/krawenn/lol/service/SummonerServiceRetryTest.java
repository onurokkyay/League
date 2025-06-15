package com.krawenn.lol.service;

import com.krawenn.lol.dto.AccountDto;
import com.krawenn.lol.dto.ChampionMasteryDto;
import com.krawenn.lol.dto.MatchDto;
import com.krawenn.lol.dto.SummonerDto;
import com.krawenn.lol.enums.Region;
import com.krawenn.lol.exception.RiotGamesSystemException;
import com.krawenn.lol.service.impl.SummonerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SummonerServiceRetryTest {

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

    // Helper methods for common test patterns
    private <T> void mockRetryWithHttpError(Class<T> responseType, T successResponse, HttpStatus errorStatus) {
        when(restTemplate.getForEntity(anyString(), eq(responseType)))
                .thenThrow(new HttpServerErrorException(errorStatus))
                .thenReturn(new ResponseEntity<>(successResponse, HttpStatus.OK));
    }

    private <T> void mockRetryWithTimeout(Class<T> responseType, T successResponse) {
        when(restTemplate.getForEntity(anyString(), eq(responseType)))
                .thenThrow(new ResourceAccessException("Connection timeout"))
                .thenReturn(new ResponseEntity<>(successResponse, HttpStatus.OK));
    }

    private <T> void mockContinuousHttpError(Class<T> responseType, HttpStatus errorStatus) {
        when(restTemplate.getForEntity(anyString(), eq(responseType)))
                .thenThrow(new HttpServerErrorException(errorStatus));
    }

    @Nested
    @DisplayName("HTTP 5xx Error Retry Tests")
    class Http5xxErrorRetryTests {
        
        @Test
        @DisplayName("Should retry on 503 Service Unavailable and eventually succeed")
        void getMatchIdsByPuuid_ShouldRetryOn503AndSucceed() {
            // Arrange
            String[] expectedMatchIds = {"match1", "match2"};
            mockRetryWithHttpError(String[].class, expectedMatchIds, HttpStatus.SERVICE_UNAVAILABLE);

            // Act
            List<String> result = summonerService.getMatchIdsByPuuid(TEST_REGION, TEST_PUUID, 0, 20);

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("match1", result.get(0));
            assertEquals("match2", result.get(1));
            
            // Verify that the method was called twice
            verify(restTemplate, times(2)).getForEntity(anyString(), eq(String[].class));
        }

        @Test
        @DisplayName("Should retry on 502 Bad Gateway and eventually succeed")
        void getAccountDtoByRiotId_ShouldRetryOn502AndSucceed() {
            // Arrange
            AccountDto expectedAccount = new AccountDto();
            expectedAccount.setPuuid(TEST_PUUID);
            mockRetryWithHttpError(AccountDto.class, expectedAccount, HttpStatus.BAD_GATEWAY);

            // Act
            AccountDto result = summonerService.getAccountDtoByRiotId(TEST_REGION, TEST_GAME_NAME, TEST_TAG_LINE);

            // Assert
            assertNotNull(result);
            assertEquals(TEST_PUUID, result.getPuuid());
            
            // Verify that the method was called twice
            verify(restTemplate, times(2)).getForEntity(anyString(), eq(AccountDto.class));
        }

        @Test
        @DisplayName("Should retry on 504 Gateway Timeout and eventually succeed")
        void getChampionMasteriesByPuuid_ShouldRetryOn504AndSucceed() {
            // Arrange
            ChampionMasteryDto[] expectedMasteries = {
                new ChampionMasteryDto(),
                new ChampionMasteryDto()
            };
            mockRetryWithHttpError(ChampionMasteryDto[].class, expectedMasteries, HttpStatus.GATEWAY_TIMEOUT);

            // Act
            List<ChampionMasteryDto> result = summonerService.getChampionMasteriesByPuuid(TEST_REGION, TEST_PUUID);

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());
            
            // Verify that the method was called twice
            verify(restTemplate, times(2)).getForEntity(anyString(), eq(ChampionMasteryDto[].class));
        }
    }

    @Nested
    @DisplayName("Connection Error Retry Tests")
    class ConnectionErrorRetryTests {
        
        @Test
        @DisplayName("Should retry on connection timeout and eventually succeed")
        void getMatchDetails_ShouldRetryOnTimeoutAndSucceed() {
            // Arrange
            MatchDto expectedMatch = new MatchDto();
            mockRetryWithTimeout(MatchDto.class, expectedMatch);

            // Act
            MatchDto result = summonerService.getMatchDetails(TEST_REGION, TEST_MATCH_ID);

            // Assert
            assertNotNull(result);
            assertEquals(expectedMatch, result);
            
            // Verify that the method was called twice
            verify(restTemplate, times(2)).getForEntity(anyString(), eq(MatchDto.class));
        }
    }

    @Nested
    @DisplayName("Max Retries Tests")
    class MaxRetriesTests {
        
        @Test
        @DisplayName("Should throw exception after max retries exceeded")
        void getSummonerByPuuid_ShouldThrowExceptionAfterMaxRetries() {
            // Arrange
            mockContinuousHttpError(SummonerDto.class, HttpStatus.SERVICE_UNAVAILABLE);

            // Act & Assert
            assertThrows(RiotGamesSystemException.class, () ->
                summonerService.getSummonerByPuuid(TEST_REGION, TEST_PUUID)
            );
            
            // Verify that the method was called multiple times (default max retries)
            verify(restTemplate, atLeast(3)).getForEntity(anyString(), eq(SummonerDto.class));
        }
    }
} 