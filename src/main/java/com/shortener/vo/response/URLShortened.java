package com.shortener.vo.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@JsonDeserialize(builder = URLShortened.URLShortenedBuilder.class)
@Builder
@Value
public class URLShortened {

    private String shortenedUrl;

    /**
     * The type Url shortened builder.
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static final class URLShortenedBuilder {
    }
}
