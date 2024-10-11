package org.example.repository;

import lombok.Data;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Data
public class Opportunity {

    private Date originalEventTime;
    private int maxDuration;
    private Map<String, Zone> zones;
    private Map<String, List<String>> positionUrlSegments;
    private int insertionRate;
}

