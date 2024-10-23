package com.example.urlshortner.exception;

public class ShortUrlNotExistsException extends Exception{

    public ShortUrlNotExistsException(String msg){
        super(msg);
    }
}
