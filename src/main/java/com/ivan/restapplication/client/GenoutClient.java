package com.ivan.restapplication.client;


import com.ivan.restapplication.dto.response.SongAnalyticsResponse;
import com.ivan.restapplication.dto.response.SongCollaborationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "genoutClient",
        url = "${genout.api.base-url}"
)
public interface GenoutClient {

    @PostMapping("/api/songs/compare")
    SongAnalyticsResponse getSongAnalytics(@RequestParam String firstId, @RequestParam String secondId);

    @PostMapping("/api/songs/collaborations")
    SongCollaborationResponse getSongCollaborations(@RequestParam String firstId, @RequestParam String secondId);

}
