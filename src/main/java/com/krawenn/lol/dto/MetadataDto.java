package com.krawenn.lol.dto;

import java.util.List;
import lombok.Data;

@Data
public class MetadataDto {
    private String dataVersion;
    private String matchId;
    private List<String> participants;
} 