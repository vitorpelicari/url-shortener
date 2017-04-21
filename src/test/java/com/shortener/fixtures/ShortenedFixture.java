package com.shortener.fixtures;

import com.shortener.model.Shortened;

public class ShortenedFixture {

    public static final String BASE_URL = "http://test.com/";

    private ShortenedFixture() {
    }

    public static Shortened validShortenedUrl(final String urlCode) {
        final Shortened shortened = new Shortened();
        shortened.setUrl(BASE_URL);
        shortened.setUrlCode(urlCode);
        shortened.setShortenedUrl(BASE_URL.concat(urlCode));
        return shortened;
    }

}
