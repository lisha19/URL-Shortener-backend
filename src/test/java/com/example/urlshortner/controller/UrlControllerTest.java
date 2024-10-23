package com.example.urlshortner.controller;

import com.example.urlshortner.exception.ShortUrlNotExistsException;
import com.example.urlshortner.model.Url;
import com.example.urlshortner.service.UrlServiceI;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = {UrlControllerTest.class})
public class UrlControllerTest {

    @InjectMocks
    private UrlController urlController;

    @Mock
    private UrlServiceI urlService;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(urlController).build();
    }

    @Test
    public void getOriginalUrlTest() throws Exception {
        String shortUrl = "shortUrl";
        String expectedOriginalUrl = "http://example.com";

        when(urlService.getOriginalUrl(shortUrl)).thenReturn(expectedOriginalUrl);

        // Build the request
        RequestBuilder requestBuilder = get("/url/shortener/{shortUrl}", shortUrl)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.content().string(expectedOriginalUrl));
    }


    @Test
    public void generateShortUrlTest() throws Exception {
        String originalUrl = "http://example.com";
        String expectedShortUrl = "shortUrl";
        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        url.setShortUrl(expectedShortUrl);
        when(urlService.generateShortUrl(originalUrl)).thenReturn(url);

        Map<String, String> body = new HashMap<>();
        body.put("url", originalUrl);

        // Build the request
        RequestBuilder requestBuilder = post("/url/shortener")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(body)); // Convert map to JSON

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(expectedShortUrl));
    }

}
