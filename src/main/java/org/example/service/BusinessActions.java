package org.example.service;

import org.example.repository.DataEntry;
import org.example.repository.Opportunity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BusinessActions {

    Map<String, Integer> numberOfShowsByPodcastShow = new HashMap<>();
    Consumer<String> podcastShowProcessor = (podcastName) -> {
        Integer numberOfShows = numberOfShowsByPodcastShow.get(podcastName);
        if (Objects.isNull(numberOfShows)) {
            numberOfShowsByPodcastShow.put(podcastName, 1);
        } else {
            numberOfShowsByPodcastShow.put(podcastName, numberOfShows + 1);
        }
    };

    public Map<String, Integer> getNumberOfShowsByShowId(List<DataEntry> podcastEvents) {
        podcastEvents.forEach(podcastEvent -> podcastShowProcessor.accept(podcastEvent.getDownloadIdentifier().getShowId()));
        return numberOfShowsByPodcastShow;
    }

    public Map<String, Integer> getNumberOfShowsByDeviceType(List<DataEntry> podcastEvents) {
        podcastEvents.forEach(podcastEvent -> podcastShowProcessor.accept(podcastEvent.getDeviceType()));
        return numberOfShowsByPodcastShow;
    }

    public List<DataEntry> getShowsFromSanFrancisco(List<DataEntry> podcastEvents) {
        return podcastEvents.stream()
                .filter(podcastEvent -> podcastEvent.getCity().equals("san francisco"))
                .collect(Collectors.toList());
    }

    public Map<String, Integer> getPrerollOpportunities(List<DataEntry> podcastEvents) {

        BiConsumer<List<Opportunity>, String> opportunitiesConsumer = (opportunities, showId) -> {
            opportunities.stream()
                    .filter(opportunity -> opportunity.getPositionUrlSegments().get("aw_0_ais.adBreakIndex").stream()
                            .anyMatch(e -> e.equals("preroll")))
                    .forEach(e -> podcastShowProcessor.accept(showId));
        };

        podcastEvents.forEach(e -> opportunitiesConsumer.accept(e.getOpportunities(), e.getDownloadIdentifier().getShowId()));

        return numberOfShowsByPodcastShow.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
    }


    public Map<String, Set<Date>> getAllPodcastsToEventDates(List<DataEntry> podcastEvents) {

        Map<String, Set<Date>> podcastToEventDates = new HashMap<>();
        podcastEvents.stream()
                .map(podcastEvent -> UtilsActions.getEpisodesDatesFromOpportunities(podcastEvent.getDownloadIdentifier().getShowId(), podcastEvent.getOpportunities()))
                .forEach(datesByShow -> {
                    if (Objects.isNull(podcastToEventDates.get(datesByShow.getKey()))) {
                        podcastToEventDates.put(datesByShow.getKey(), datesByShow.getValue());
                    } else {
                        Set<Date> ceva = datesByShow.getValue();
                        ceva.addAll(podcastToEventDates.get(datesByShow.getKey()));
                        podcastToEventDates.put(datesByShow.getKey(), ceva);
                    }
                });
        return podcastToEventDates;
    }


    public Map<String, List<LocalDateTime>> getAllPodcastsToSortedEventDates(Map<String, Set<Date>> podcastsToEventDates) {

        return podcastsToEventDates.entrySet().stream()
                .map(podcastToEventDates -> new AbstractMap.SimpleEntry<>(podcastToEventDates.getKey(), UtilsActions.getSortedDates(podcastToEventDates.getValue())))
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
    }

    public Map<String, List<LocalDateTime>> getWeeklyPodcastsToEventDates(Map<String, List<LocalDateTime>> podcastsToSortedEventDates) {

        return podcastsToSortedEventDates.entrySet().stream()
                .filter(e -> UtilsActions.isExactlySevenDaysBetweenDates(e.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
