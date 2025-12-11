package com.ivan.restapplication.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SongCollaborationResponse {
    List<ArtistResponse> sharedArtists;
    List<CollabEdge> sharedCollaborations;

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ArtistResponse {
        private int id;
        private String name;
        private String role;
        private String imageUrl;
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class CollabEdge {
        private int src;
        private int dst;
    }
}


