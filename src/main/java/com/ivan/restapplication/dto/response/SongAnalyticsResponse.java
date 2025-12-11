package com.ivan.restapplication.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SongAnalyticsResponse {

    TrackResponse firstTrack;
    TrackResponse secondTrack;

    Map<String, Double> similarityMetrics;
    Map<String, Double> popularityFactors;
    Map<String, Double> styleFeatures;
    Map<String, Object> temporalFeatures;
    Double similarityScore;

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class TrackResponse {
        String title;
        String artist;
        String releaseYear;
        String headerImageUrl;
    }
}
