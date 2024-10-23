package com.example.urlshortner.service;

import com.example.urlshortner.exception.ShortUrlNotExistsException;
import com.example.urlshortner.model.Url;
import com.example.urlshortner.repository.UrlRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class UrlServiceImplTest {

    @InjectMocks
    private UrlServiceImpl urlService;

    @Mock
    private UrlRepository urlRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getOriginalUrlTest_WhenUrlPresentInDatabase_NoException() throws ShortUrlNotExistsException {
        String shortUrl = "shortUrl";
        String originalUrl = "http://example.com";
        Url url = new Url();
        url.setShortUrl(shortUrl);
        url.setOriginalUrl(originalUrl);
        url.setId(1);

        when(urlRepository.findByShortUrl(any())).thenReturn(url);

        String output = urlService.getOriginalUrl(shortUrl);

        assertEquals(originalUrl,output);
    }

    @Test(expected = ShortUrlNotExistsException.class)
    public void getOriginalUrlTest_WhenUrlNotInDatabase_ThrowsException() throws ShortUrlNotExistsException {
        String shortUrl = "shortUrl";

        when(urlRepository.findByShortUrl(any())).thenReturn(null);

        String output = urlService.getOriginalUrl(shortUrl);
    }

    @Test
    public void generateShortUrlTest_WhenUrlNotInDatabase_ShouldCreateNewShortUrl(){
        String originalUrl = "http://example.com";
        String expectedShortUrl = "bf19bedb";

        when(urlRepository.findByOriginalUrl(any())).thenReturn(null);
        when(urlRepository.findByShortUrl(any())).thenReturn(null);
        when(urlRepository.save(any())).thenAnswer(invocation  -> invocation.getArgument(0));

        Url result = urlService.generateShortUrl(originalUrl);

        assertNotNull(result);
        assertEquals(originalUrl, result.getOriginalUrl());
        assertEquals(expectedShortUrl, result.getShortUrl());
    }

    @Test
    public void generateShortUrlTest_WhenUrlPresentInDatabase_ShouldReturnExistingShortUrl(){
        String originalUrl = "http://example.com";
        String existingShortUrl = "extShortUrl";
        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        url.setShortUrl(existingShortUrl);

        when(urlRepository.findByOriginalUrl(any())).thenReturn(url);

        Url result = urlService.generateShortUrl(originalUrl);

        assertNotNull(result);
        assertEquals(originalUrl, result.getOriginalUrl());
        assertEquals(existingShortUrl, result.getShortUrl());
    }




}
