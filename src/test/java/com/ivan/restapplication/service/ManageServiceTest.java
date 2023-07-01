package com.ivan.restapplication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
public class ManageServiceTest {

    private final List<String> terms = new ArrayList<>();
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private ManageService manageService;
    @Autowired
    private TopArtistService topArtistService;
    private MockRestServiceServer server;

    @BeforeEach
    public void setUp() {
        server = MockRestServiceServer.createServer(restTemplate);
        terms.add("short_term");
        terms.add("medium_term");
        terms.add("long_term");
    }

    @Test
    public void getFollowedArtists() throws JsonProcessingException {
        String apiResponse1 = "{ \"artists\": { \"items\": [ { \"name\": \"Drake\" }, { \"name\": \"Getter\" } ], \"next\": \"https://api.spotify.com/v1/me/following?type=artist&limit=50&offset=50\" } }";
        String apiResponse2 = "{ \"artists\": { \"items\": [ { \"name\": \"Prof\" }, { \"name\": \"Slipknot\" } ], \"next\": \"null\" } }";
        String apiResponse3 = "[{\"name\":\"Drake\"},{\"name\":\"Getter\"},{\"name\":\"Prof\"},{\"name\":\"Slipknot\"}]";

        server.expect(requestTo("https://api.spotify.com/v1/me/following?type=artist&limit=50"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(apiResponse1, MediaType.APPLICATION_JSON));

        server.expect(requestTo("https://api.spotify.com/v1/me/following?type=artist&limit=50&offset=50"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(apiResponse2, MediaType.APPLICATION_JSON));

        JsonNode actual = manageService.getFollowedArtists();

        server.verify();

        assertNotNull(actual);
        assertTrue(actual.isArray());
        assertThat(actual).isNotEmpty();
        assertThat(actual.toString()).isEqualTo(apiResponse3);

    }

    @Test
    public void testGetSuggestedArtists() throws Exception {
        String topArtistsResponse = "{\"items\": [{\"id\" : \"Prof\"}, {\"id\" : \"Getter\"} , {\"id\" : \"Eminem\"}]}";
        JsonNode artists = mapper.readTree(topArtistsResponse);

        String followedNode = "{\"items\": [{\"id\" : \"Prof\"}]}";
        JsonNode followed = mapper.readTree(followedNode);
        manageService.setFollowedArtistNode(followed);

        ArrayNode suggest = mapper.createArrayNode();

        for (String term : terms) {
            server.expect(requestTo("https://api.spotify.com/v1/me/top/artists?limit=50&time_range=" + term))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(withSuccess(topArtistsResponse, MediaType.APPLICATION_JSON));
        }

        for (JsonNode artist : artists) {
            for (JsonNode el : artist) {
                if (!followed.findValuesAsText("id").contains(el.findValue("id").asText()) && !suggest.findValuesAsText("id").contains(el.findValue("id").asText())) {
                    suggest.add(el);
                }
            }
        }

        JsonNode actual = manageService.getSuggestedArtists();
        server.verify();

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(suggest, actual);
        Assertions.assertTimeout(Duration.ofMillis(300), () -> {
        });
    }

}
