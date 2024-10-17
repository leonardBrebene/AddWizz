package org.example.service;

import org.example.repository.DataEntry;
import org.example.repository.Opportunity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UtilsActions {

    public Map.Entry<String, Integer> getMostNumerousShow(Map<String, Integer> countByPodcastShow) {
        return countByPodcastShow.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

    }

    public static Map.Entry<String, Set<Date>> getEpisodesDatesFromOpportunities(String show, List<Opportunity> opportunities) {
        Set<Date> dates = opportunities.stream()
                .map(Opportunity::getOriginalEventTime)
                .collect(Collectors.toCollection(HashSet::new));
        return new AbstractMap.SimpleEntry<>(show, dates);
    }

    public static List<LocalDateTime> getSortedDates(Set<Date> eventDates) {
        return eventDates.stream().sorted()
                .map(date -> date.toInstant()
                        .atZone(ZoneId.of("UTC"))
                        .toLocalDateTime())
                .collect(Collectors.toList());
    }

    public static boolean isExactlySevenDaysBetweenDates(List<LocalDateTime> dates) {
        return IntStream.range(0, dates.size() - 1)
                .anyMatch(i -> dates.get(i).plusDays(7).equals(dates.get(i + 1)));
    }
}
