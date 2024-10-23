package com.example.urlshortner.exception;

public class InvalidUrlException extends Exception{

    public InvalidUrlException(String msg){
        super(msg);
    }
}
