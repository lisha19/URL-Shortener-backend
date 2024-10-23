package com.example.urlshortner.repository;

import com.example.urlshortner.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlRepository extends JpaRepository<Url,Integer> {

    Url findByShortUrl(String shortUrl);

    Url findByOriginalUrl(String originalUrl);

}