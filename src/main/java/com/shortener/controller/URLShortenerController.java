package com.shortener.controller;

import com.shortener.service.URLShortenerService;
import com.shortener.vo.request.URLShortenerRequestVo;
import com.shortener.vo.response.URLShortened;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
 * The type Url shortener controller.
 */
@Slf4j
@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class URLShortenerController {

    private URLShortenerService urlShortenerService;

    /**
     * Instantiates a new Url shortener controller.
     *
     * @param urlShortenerService the url shortener service
     */
    public URLShortenerController(final URLShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    /**
     * Shortens the passed url.
     *
     * @param requestVo the request vo
     * @return the url shortened
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public URLShortened shortenUrl(@RequestBody @Valid final URLShortenerRequestVo requestVo) {
        log.debug("BEGIN shortenUrl: requestVo={}", requestVo);
        final URLShortened urlShortened = urlShortenerService.shortenUrl(requestVo.getUrl());
        log.debug("END shortenUrl. result={}", urlShortened);
        return urlShortened;
    }

    /**
     * Gets url by urlCode and redirects in case the code exists on db.
     *
     * @param urlCode the urlCode hash used to retrieve the real url
     */
    @GetMapping(value = "/{urlCode}")
    public void getUrl(HttpServletResponse response, @PathVariable final String urlCode) throws IOException {
        log.debug("BEGIN getUrl: urlCode={}", urlCode);
        try {
            final String url = urlShortenerService.getUrl(urlCode);
            log.debug("END getUrl. result={}", url);
            response.sendRedirect(url);
        } catch (EntityNotFoundException ex) {
            log.error("Error trying to retrieve original url for " + urlCode, ex);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
