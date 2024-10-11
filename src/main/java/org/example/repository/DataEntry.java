package org.example.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DataEntry {

    @JsonProperty("downloadIdentifier")
    private DownloadIdentifier downloadIdentifier;

    @JsonProperty("opportunities")
    private List<Opportunity> opportunities;

    @JsonProperty("agency")
    private int agency;

    @JsonProperty("deviceType")
    private String deviceType;

    @JsonProperty("country")
    private String country;

    @JsonProperty("city")
    private String city;

    @JsonProperty("listenerId")
    private String listenerId;


}
