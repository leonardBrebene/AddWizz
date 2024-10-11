package org.example;

import org.example.repository.DataEntry;
import org.example.service.BusinessActions;
import org.example.service.DatabaseActions;
import org.example.service.UtilsActions;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class AppTest {

    DatabaseActions databaseActions = new DatabaseActions();
    BusinessActions businessActions = new BusinessActions();
    UtilsActions utilsActions = new UtilsActions();

    @Test
    public void testMostListenedPodcastShow() {


        List<DataEntry> podcastEvents = databaseActions.getEntriesFromDownloads();
        List<DataEntry> podcastEventsFromSanFrancisco=businessActions.getShowsFromSanFrancisco(podcastEvents);

        Map<String, Integer> numberOfShowsByPodcastShow = businessActions.getNumberOfShowsByShowId(podcastEventsFromSanFrancisco);

        Map.Entry<String, Integer> mostPopularShow = utilsActions.getMostNumerousShow(numberOfShowsByPodcastShow);

        System.out.println("Most popular show is: "+mostPopularShow.getKey()+"\n" +
                "Number of downloads is: " + mostPopularShow.getValue());

        assertEquals("Who Trolled Amber", mostPopularShow.getKey());
        assertEquals(24, (int)mostPopularShow.getValue());

    }

    @Test
    public void testMostPopularDeviceToListen() {

        List<DataEntry> podcastEvents = databaseActions.getEntriesFromDownloads();

        Map<String, Integer> numberOfShowsByPodcastShow = businessActions.getNumberOfShowsByDeviceType(podcastEvents);

        Map.Entry<String, Integer> mostPopularShow = utilsActions.getMostNumerousShow(numberOfShowsByPodcastShow);

        System.out.println("Most popular device is: "+mostPopularShow.getKey()+"\n" +
                "Number of downloads is: " + mostPopularShow.getValue());

        assertEquals("mobiles & tablets", mostPopularShow.getKey());
        assertEquals(60, (int)mostPopularShow.getValue());


    }

    @Test
    public void testPrerollOpportunities() {

        List<DataEntry> podcastEvents = databaseActions.getEntriesFromDownloads();

        Map<String, Integer> numberOfShowsByPodcastShow = businessActions.getPrerollOpportunities(podcastEvents);
        numberOfShowsByPodcastShow.forEach((key, value) -> System.out.println("Show Id: " + key + ", Preroll Opportunity Number: " + value));
    }


}
