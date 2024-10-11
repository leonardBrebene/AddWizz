package org.example.service;

import org.example.repository.DataEntry;
import org.example.repository.Opportunity;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
}
