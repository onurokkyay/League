package com.krawenn.lol.exception;

public class SummonerNotFoundException extends RuntimeException {
    public SummonerNotFoundException(String gameName, String tagLine) {
        super("Data not found - No results found for player with riot id " + gameName + "#" + tagLine);
    }
} 