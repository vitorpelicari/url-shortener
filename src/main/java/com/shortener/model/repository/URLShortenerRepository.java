package com.shortener.model.repository;

import com.shortener.model.Shortened;
import org.springframework.data.repository.CrudRepository;

public interface URLShortenerRepository extends CrudRepository<Shortened, String> {

    Shortened findByUrlEquals(final String url);

    Shortened findByShortenedUrlEquals(final String shortenedUrl);

    Shortened findByUrlCodeEquals(final String urlCode);
}
