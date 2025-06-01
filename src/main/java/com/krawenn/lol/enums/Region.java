package com.krawenn.lol.enums;

import lombok.Getter;

@Getter
public enum Region {
    BR1("br1", "americas"),
    EUN1("eun1", "europe"),
    EUW1("euw1", "europe"),
    JP1("jp1", "asia"),
    KR("kr", "asia"),
    LA1("la1", "americas"),
    LA2("la2", "americas"),
    NA1("na1", "americas"),
    OC1("oc1", "sea"),
    TR1("tr1", "europe"),
    RU("ru", "europe");

    private final String apiCode;
    private final String accountRoute;

    Region(String apiCode, String accountRoute) {
        this.apiCode = apiCode;
        this.accountRoute = accountRoute;
    }

    public static Region fromString(String code) {
        for (Region region : Region.values()) {
            if (region.apiCode.equalsIgnoreCase(code)) {
                return region;
            }
        }
        throw new IllegalArgumentException("Invalid region code: " + code);
    }
} 