package org.example;

import org.example.repository.DataEntry;
import org.example.service.BusinessActions;
import org.example.service.DatabaseActions;
import org.example.service.UtilsActions;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class AppTest {


    DatabaseActions databaseActions = new DatabaseActions();
    BusinessActions businessActions = new BusinessActions();
    UtilsActions utilsActions = new UtilsActions();
    List<DataEntry> podcastEvents = databaseActions.getEntriesFromDownloads();

    @Test
    public void testMostListenedPodcastShow() {

        List<DataEntry> podcastEventsFromSanFrancisco = businessActions.getShowsFromSanFrancisco(podcastEvents);

        Map<String, Integer> numberOfShowsByPodcastShow = businessActions.getNumberOfShowsByShowId(podcastEventsFromSanFrancisco);

        Map.Entry<String, Integer> mostPopularShow = utilsActions.getMostNumerousShow(numberOfShowsByPodcastShow);

        System.out.println("Most popular show is: " + mostPopularShow.getKey() + "\n" +
                "Number of downloads is: " + mostPopularShow.getValue());

        assertEquals("Who Trolled Amber", mostPopularShow.getKey());
        assertEquals(24, (int) mostPopularShow.getValue());

    }

    @Test
    public void testMostPopularDeviceToListen() {

        Map<String, Integer> numberOfShowsByPodcastShow = businessActions.getNumberOfShowsByDeviceType(podcastEvents);

        Map.Entry<String, Integer> mostPopularShow = utilsActions.getMostNumerousShow(numberOfShowsByPodcastShow);

        System.out.println("Most popular device is: " + mostPopularShow.getKey() + "\n" +
                "Number of downloads is: " + mostPopularShow.getValue());

        assertEquals("mobiles & tablets", mostPopularShow.getKey());
        assertEquals(60, (int) mostPopularShow.getValue());


    }

    @Test
    public void testPrerollOpportunities() {

        Map<String, Integer> numberOfShowsByPodcastShow = businessActions.getPrerollOpportunities(podcastEvents);
        numberOfShowsByPodcastShow.forEach((key, value) -> System.out.println("Show Id: " + key + ", Preroll Opportunity Number: " + value));
    }

    @Test
    public void testWeeklyShows() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E HH:mm");

        Map<String, Set<Date>> podcastsToEventDates = businessActions.getAllPodcastsToEventDates(podcastEvents);
        Map<String, List<LocalDateTime>> allPodcastsToSortedEventDates = businessActions.getAllPodcastsToSortedEventDates(podcastsToEventDates);
        Map<String, List<LocalDateTime>> weeklyPodcastsToEventDates = businessActions.getWeeklyPodcastsToEventDates(allPodcastsToSortedEventDates);

        System.out.println("Weekly shows are: \n\n");
        weeklyPodcastsToEventDates.forEach((key, value) -> System.out.println(key + " - " + value.get(0).format(formatter)));

    }


}
