package org.example.service;

import org.example.repository.DataEntry;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UtilsActions {

    public Map.Entry<String, Integer> getMostNumerousShow(Map<String, Integer> countByPodcastShow) {
        return countByPodcastShow.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

    }
}
