package com.ivan.restapplication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TopTracksServiceTest {

    private final List<String> terms = new ArrayList<>();
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TopTrackService topTrackService;
    @Autowired
    private ObjectMapper mapper;

    private MockRestServiceServer server;

    @BeforeEach
    public void init() {
        server = MockRestServiceServer.createServer(restTemplate);
        terms.add("short_term");
        terms.add("medium_term");
        terms.add("long_term");
    }

    @Test
    public void findTopTracksTest() throws JsonProcessingException {
        String responseBody = "{\"items\": [{\"id\": \"track1\"}, {\"id\": \"track2\"}]}";
        server.expect(MockRestRequestMatchers.requestTo("https://api.spotify.com/v1/me/top/tracks?limit=50&time_range=" + terms.stream().findAny().orElseThrow()))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andExpect(MockRestRequestMatchers.queryParam("time_range", terms.stream().findAny().orElseThrow()))
                .andRespond(MockRestResponseCreators.withSuccess(responseBody, MediaType.APPLICATION_JSON));

        JsonNode actual = topTrackService.findTopTracks(terms.stream().findAny().orElseThrow());
        server.verify();
        assertThat(actual).isNotEmpty();
        assertThat(actual.toString()).isEqualTo("[{\"id\":\"track1\"},{\"id\":\"track2\"}]");
    }

    @Test
    public void testGetTopTracksIds() throws Exception {
        String responseBody = "{\"items\": [{\"id\": \"track1\"}, {\"id\": \"track2\"}]}";
        server.expect(MockRestRequestMatchers.requestTo("https://api.spotify.com/v1/me/top/tracks?limit=50&time_range=" + terms.stream().findAny().orElseThrow()))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withSuccess(responseBody, MediaType.APPLICATION_JSON));

        String actual = topTrackService.getTopTracksIds(terms.stream().findAny().orElseThrow());
        server.verify();
        assertThat(actual).isEqualTo("track1,track2");
    }

    @Test
    public void testGetTrackList() throws Exception {
        String topTracksResponse = "{\"items\": [{\"id\": \"track1\"}, {\"id\": \"track2\"}]}";
        server.expect(MockRestRequestMatchers.requestTo("https://api.spotify.com/v1/me/top/tracks?limit=50&time_range=" + terms.stream().findAny().orElseThrow()))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withSuccess(topTracksResponse, MediaType.APPLICATION_JSON));

        String featuresResponse = "{\"audio_features\": [{\"tempo\": \"0.168\", \"id\": \"track1\"}, {\"tempo\": \"0.168\", \"id\": \"track2\"}]}";
        JsonNode featureJson = mapper.readTree(featuresResponse);
        server.expect(MockRestRequestMatchers.requestTo("https://api.spotify.com/v1/audio-features?ids=track1,track2"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withSuccess(featuresResponse, MediaType.APPLICATION_JSON));

        Map<Float, JsonNode> expected = new HashMap<>();  //Map with float value of feature, and track node
        if (!featureJson.get("audio_features").toString().equals("[null]")) {
            featureJson.get("audio_features")
                    .forEach(f -> expected
                            .put(f.get("tempo").floatValue(), f.get("track_href")));     //Forming map
        }


        Map<Float, JsonNode> actual = topTrackService.getTrackList(terms.stream().findAny().orElseThrow(), "tempo");
        server.verify();
        assertThat(actual).isNotEmpty();
        assertThat(actual).isEqualTo(expected);
    }
}
