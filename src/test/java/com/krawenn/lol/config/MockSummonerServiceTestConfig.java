package com.krawenn.lol.config;


import com.krawenn.lol.service.impl.SummonerService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MockSummonerServiceTestConfig {
    @Bean
    public SummonerService summonerService() {
        return Mockito.mock(SummonerService.class);
    }
}
