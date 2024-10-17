package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.repository.DataEntry;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseActions {

    final public static String FILE_PATH = "src/test/java/resources/downloads.txt";

    public List<DataEntry> getEntriesFromDownloads() {
        ObjectMapper objectMapper = new ObjectMapper();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {

            return reader.lines().map(line -> {
                        try {
                            return objectMapper.readValue(line, DataEntry.class);
                        } catch (JsonProcessingException e) {
                            System.out.println("An error occurred while parsing data from downloads.txt file:" + e.getMessage());
                            return new DataEntry();
                        }
                    })
                    .collect(Collectors.toList());

        } catch (IOException e) {
            throw new RuntimeException("downloads.txt file could not be read" + e.getMessage());
        }
    }
}
