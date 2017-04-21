package com.shortener.model;


import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;
import java.io.Serializable;

@Data
@RedisHash("shortened")
public class Shortened implements Serializable {

    @Id
    private String id;

    @Indexed
    private String url;

    @Indexed
    private String shortenedUrl;

    @Indexed
    private String urlCode;

}
