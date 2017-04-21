package com.shortener.vo.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@JsonDeserialize(builder = URLShortenerRequestVo.URLShortenerRequestVoBuilder.class)
@Builder
@Value
public class URLShortenerRequestVo {

    @URL
    @NotBlank
    private String url;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class URLShortenerRequestVoBuilder {
    }
}
