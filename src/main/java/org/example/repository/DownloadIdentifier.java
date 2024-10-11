package org.example.repository;


import lombok.Data;

@Data
public class DownloadIdentifier {
    private String client;
    private int publisher;
    private String podcastId;
    private String showId;
    private String episodeId;
    private String downloadId;
}
