package com.example.urlshortner.service;

import com.example.urlshortner.exception.InvalidUrlException;
import com.example.urlshortner.exception.ShortUrlNotExistsException;
import com.example.urlshortner.model.Url;
import com.example.urlshortner.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.urlshortner.logic.GenerateShortUrlUtil.getShortUrl;

@Service
public class UrlServiceImpl implements UrlServiceI{
    @Autowired
    private UrlRepository urlRepository;


    public String getOriginalUrl(String shortUrl) throws ShortUrlNotExistsException {
        Url urlFromDB = urlRepository.findByShortUrl(shortUrl);
        if(urlFromDB == null){
            throw new ShortUrlNotExistsException("Given short url does not exist");
        }
        return urlFromDB.getOriginalUrl();
    }

    public Url generateShortUrl(String originalUrl) {

        // check if original url already exists
        Url urlFromDB = urlRepository.findByOriginalUrl(originalUrl);
        if(urlFromDB!=null){
            return urlFromDB;
        }

        Url urlObject = new Url();
        urlObject.setOriginalUrl(originalUrl);

        String shortUrl = getShortUrl(originalUrl);
        // check for collisions
        while (urlRepository.findByShortUrl(shortUrl) != null){
            String saltedUrl = originalUrl + System.nanoTime();
            shortUrl = getShortUrl(saltedUrl);
        }

        urlObject.setShortUrl(shortUrl);
        return urlRepository.save(urlObject);
    }
}