package com.krawenn.lol.dto;

import java.util.List;
import lombok.Data;

@Data
public class MatchDto {
    private MetadataDto metadata;
    private InfoDto info;
}
