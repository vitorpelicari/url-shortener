# Project - URL Shortener

[![Build Status](https://travis-ci.org/vitorpelicari/url-shortener.svg?branch=master)](https://travis-ci.org/vitorpelicari/url-shortener)

## Requirements
* Java SE 8
* Gradle 3.5
* Redis 3.2.8

## Starting Redis
    
    docker pull redis
    docker run --name url-shortener-redis --net=host -d redis

## Building Project

    gradle clean build

## Run Application

    gradle bootRun

## Usage
    
    Application will start on port 8080, it will expose 2 endpoints
    
    - GET: http://localhost:8080/f140b39e
        where f140b39e is a code of a shortened URL
        response 200 will result in a URL redirect to the url originally shortened
        
    - POST: http://localhost:8080
        where the body should be:
            {
            	"url": "http://urltobeshortened.com"
            }