package com.example.urlshortner.controller;


import com.example.urlshortner.exception.InvalidUrlException;
import com.example.urlshortner.exception.ShortUrlNotExistsException;
import com.example.urlshortner.model.Url;
import com.example.urlshortner.service.UrlServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.example.urlshortner.logic.GenerateShortUrlUtil.isUrlValid;

@RestController
//@CrossOrigin(origins = "http://localhost:3000")

@RequestMapping("url/shortener")
//@CrossOrigin(origins = "http://localhost:3000")
public class UrlController {

    @Autowired
    private UrlServiceI urlService;


    @GetMapping("/{shortUrl}")
    public ResponseEntity<String> getOriginalUrl(@PathVariable String shortUrl) throws ShortUrlNotExistsException {
        String originalUrl = urlService.getOriginalUrl(shortUrl); // Get the original URL from the service
        if (originalUrl == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Not found
        }

        return ResponseEntity.status(HttpStatus.FOUND).body(originalUrl);

    }

    @PostMapping
    public ResponseEntity<String> generateShortUrl(@RequestBody Map<String, String> body) throws InvalidUrlException {
        String originalUrl = body.get("url");

        if(! isUrlValid(originalUrl)) {
            throw new InvalidUrlException("URL is not valid");
        }

        Url generatedUrl = urlService.generateShortUrl(originalUrl);
        if (generatedUrl == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(generatedUrl.getShortUrl());
    }


}
