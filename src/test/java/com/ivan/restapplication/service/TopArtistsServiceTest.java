package com.ivan.restapplication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

@SpringBootTest
public class TopArtistsServiceTest {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TopArtistService topArtistService;

    @Autowired
    private ObjectMapper mapper;

    private MockRestServiceServer mockServer;

    private final List<String> terms = new ArrayList<>();

    @BeforeEach
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        terms.add("short_term");
        terms.add("medium_term");
        terms.add("long_term");
    }

    @Test
    public void testFindTopArtists() throws Exception {
        String apiResponse = "{\"items\": [\"Artist 1\", \"Artist 2\"]}";
        mockServer.expect(requestTo("https://api.spotify.com/v1/me/top/artists?limit=50&time_range=" + terms.stream().findAny().orElseThrow()))
                .andExpect(method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withSuccess(apiResponse, MediaType.APPLICATION_JSON));

        JsonNode actual = topArtistService.findTopArtists(terms.stream().findAny().orElseThrow());
        mockServer.verify();
        assertThat(actual.toString()).isEqualTo("[\"Artist 1\",\"Artist 2\"]");
    }

    @Test
    public void testFindTopArtistsAllTime() throws JsonProcessingException {
        ArrayNode node = mapper.createArrayNode();
        String apiResponse = "{\"items\": [\"Artist 1\", \"Artist 2\"]}";

        for (String term: terms) {
            mockServer.expect(requestTo("https://api.spotify.com/v1/me/top/artists?limit=50&time_range=" + term))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(MockRestResponseCreators.withSuccess(apiResponse, MediaType.APPLICATION_JSON));
            node.add(Mockito.anyString());
        }

        JsonNode actual = topArtistService.findTopArtistsAllTime();
        mockServer.verify();
        assertThat(node).isNotEmpty();
        assertThat(actual.toString()).isEqualTo("[[\"Artist 1\",\"Artist 2\"],[\"Artist 1\",\"Artist 2\"],[\"Artist 1\",\"Artist 2\"]]");
    }

}