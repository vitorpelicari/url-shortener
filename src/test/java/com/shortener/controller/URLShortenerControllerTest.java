package com.shortener.controller;

import com.shortener.service.URLShortenerService;
import com.shortener.vo.response.URLShortened;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = URLShortenerController.class)
public class URLShortenerControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private URLShortenerService urlShortenerService;

    @Test
    public void testShortenUrl() throws Exception {
        given(urlShortenerService.shortenUrl(eq("http://success.url.com"))).willReturn(
            URLShortened.builder().shortenedUrl("http://success.url.com").build()
        );
        final String payload = "{" +
            "\"url\": \"http://success.url.com\"" +
            "}";
        mvc.perform(post("/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(urlShortenerService).shortenUrl("http://success.url.com");
    }

    @Test
    public void testShortenNullUrl() throws Exception {
        final String payload = "{" +
            "\"url\": \"\"" +
            "}";
        mvc.perform(post("/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testShortenInvalidUrl() throws Exception {
        final String payload = "{" +
            "\"url\": \"htt://asdf.asd\"" +
            "}";
        mvc.perform(post("/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetUrl() throws Exception {
        given(urlShortenerService.getUrl(anyString())).willReturn("http://success.com/");
        mvc.perform(get("/" + "someHashedValue"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("http://success.com/"));
    }

    @Test
    public void testGetUrlEntityNotFound() throws Exception {
        given(urlShortenerService.getUrl(anyString()))
            .willThrow(new EntityNotFoundException("URL not found for code 123456"));
        mvc.perform(get("/" + "someHashedValue"))
            .andExpect(status().isNotFound());
    }

}