package org.example;

import org.example.repository.DataEntry;
import org.example.repository.Opportunity;
import org.example.service.DatabaseActions;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static Map.Entry<String, Set<Date>> getEpisodes(String show, List<Opportunity> opportunities) {
        Set<Date> dates = opportunities.stream().map(Opportunity::getOriginalEventTime)
                .collect(Collectors.toCollection(HashSet::new));
        return new AbstractMap.SimpleEntry<>(show, dates);
    }
    public static boolean metodaName(List<LocalDateTime> dates){
        return IntStream.range(0, dates.size() - 1)
                .anyMatch(i->dates.get(i).plusDays(7).equals( dates.get(i +1)));
    }

    public static void main(String[] args) {
        System.out.println("Hello Leo");
        String filePath = "src/main/java/org/example/downloads.txt"; // replace with the actual file path

        DatabaseActions databaseActions = new DatabaseActions();
        List<DataEntry> podcastEntries = databaseActions.getEntriesFromDownloads();
        podcastEntries.forEach(downloadData -> System.out.println("City: " + downloadData.getCity()));

        Map<String, Integer> myMap = new HashMap<>();
        Consumer<String> messageConsumer = (showId) -> {
            Integer ceva = myMap.get(showId);
            if (Objects.isNull(ceva)) {
                myMap.put(showId, 1);
            } else {
                myMap.put(showId, ceva + 1);
            }
        };
//        podcastEntries.forEach(e -> messageConsumer.accept(e.getDownloadIdentifier().getShowId()));
//        Map.Entry<String, Integer> maxEntry = myMap.entrySet().stream()
//                .max(Map.Entry.comparingByValue()) // Find the entry with the maximum value
//                .orElse(null);
//
//        myMap.clear();
//
//        podcastEntries.forEach(e -> messageConsumer.accept(e.getDeviceType()));
//        Map.Entry<String, Integer> maxEntry2 = myMap.entrySet().stream()
//                .max(Map.Entry.comparingByValue()) // Find the entry with the maximum value
//                .orElse(null);
//
//        myMap.clear();


        BiConsumer<List<Opportunity>, String> opportunitiesConsumer = (opportunities, showId) -> {
            opportunities.stream()
                    .filter(opportunity -> opportunity.getPositionUrlSegments().get("aw_0_ais.adBreakIndex").stream()
                            .anyMatch(e -> e.equals("preroll")))
                    .forEach(e -> messageConsumer.accept(showId));
        };

        podcastEntries.forEach(e -> opportunitiesConsumer.accept(e.getOpportunities(), e.getDownloadIdentifier().getShowId()));
        System.out.println("Exercitiul 5 " + myMap);

//        long intrari = podcastEntries.stream()
//                .mapToLong(e -> e.getOpportunities().stream().count())
//                .sum();
//        System.out.println("sunt oportunitati:" + intrari);


        Map<String, Set<Date>> episodes = new HashMap<>();

        podcastEntries.stream().map(e -> getEpisodes(e.getDownloadIdentifier().getShowId(), e.getOpportunities()))
                .forEach(e -> {
                    if (Objects.isNull(episodes.get(e.getKey()))) {
                        episodes.put(e.getKey(), e.getValue());
                    } else {
                        Set<Date> ceva = e.getValue();
                        ceva.addAll(episodes.get(e.getKey()));
                        episodes.put(e.getKey(), ceva);
                    }
                });

        Map<String, List<LocalDateTime>> episodesSortedAndParsed = episodes.entrySet().stream()
                .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue().stream()
                        .sorted()
                        .map(date->date.toInstant()
                                .atZone(ZoneId.of("UTC")) // Use the system's default timezone
                                .toLocalDateTime())
                        .collect(Collectors.toList())))
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));

        Map<String, List<LocalDateTime>> episodesFiltered= episodesSortedAndParsed.entrySet().stream()
                        .filter(e->metodaName(e.getValue()))
                .collect(Collectors.toMap(e->e.getKey(), e->e.getValue()));

        System.out.println("Final");
    }
}
