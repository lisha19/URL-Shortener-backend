package com.example.urlshortner.logic;

import com.google.common.hash.Hashing;
import org.apache.commons.validator.routines.UrlValidator;
import java.nio.charset.StandardCharsets;

public class GenerateShortUrlUtil {

    public static String getShortUrl(String url) {
       return Hashing.murmur3_32_fixed().hashString(url, StandardCharsets.UTF_8).toString();
    }

    public static boolean isUrlValid(String url) {
        UrlValidator urlValidator = new UrlValidator(
                new String[]{"http","https"}
        );
        return urlValidator.isValid(url);
    }

}