package com.shortener.service;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.shortener.fixtures.ShortenedFixture;
import com.shortener.model.Shortened;
import com.shortener.model.repository.URLShortenerRepository;
import com.shortener.vo.response.URLShortened;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityNotFoundException;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;


@RunWith(MockitoJUnitRunner.class)
public class URLShortenerServiceTest {

    @InjectMocks
    private URLShortenerService service;

    @Mock
    private URLShortenerRepository repository;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Captor
    private ArgumentCaptor<Shortened> shortenedCaptor;

    private HashFunction hashing = Hashing.murmur3_32(100);
    private HashFunction mismatchHashingSeed = Hashing.murmur3_32(101);

    @Test
    public void testShortenUrl() throws Exception {
        given(repository.findByShortenedUrlEquals(anyString())).willReturn(null);
        given(repository.findByUrlEquals(anyString())).willReturn(null);
        final String url = "http://testshortenurl.com";
        final Shortened shortened = new Shortened();
        shortened.setUrl(url);
        shortened.setId("abcdef");

        given(repository.save(any(Shortened.class))).willReturn(shortened);

        final URLShortened urlShortened = service.shortenUrl(ShortenedFixture.BASE_URL);

        verify(repository, times(2)).save(shortenedCaptor.capture());
        // Getting last save iteration
        final Shortened s = shortenedCaptor.getAllValues().get(1);

        final String hashedId = hashing.hashString("abcdef", StandardCharsets.UTF_8).toString();
        Assertions.assertThat(urlShortened.getShortenedUrl()).contains(hashedId);
        assertThat(urlShortened.getShortenedUrl(), is(s.getShortenedUrl()));
        assertThat(s.getUrlCode(), is(hashedId));
    }

    @Test
    public void testShortenUrlDifferentSeed() throws Exception {
        given(repository.findByShortenedUrlEquals(anyString())).willReturn(null);
        given(repository.findByUrlEquals(anyString())).willReturn(null);
        final String url = "http://testshortenurl.com";
        final Shortened shortened = new Shortened();
        shortened.setUrl(url);
        shortened.setId("abcdef");

        given(repository.save(any(Shortened.class))).willReturn(shortened);

        final URLShortened urlShortened = service.shortenUrl(ShortenedFixture.BASE_URL);

        verify(repository, times(2)).save(shortenedCaptor.capture());
        // Getting last save iteration
        final Shortened s = shortenedCaptor.getAllValues().get(1);

        final String hashedId = mismatchHashingSeed.hashString("abcdef", StandardCharsets.UTF_8).toString();
        Assertions.assertThat(urlShortened.getShortenedUrl()).doesNotContain(hashedId);
        assertThat(urlShortened.getShortenedUrl(), is(s.getShortenedUrl()));
        assertThat(s.getUrlCode(), is(not(hashedId)));
    }

    @Test
    public void testShortenShortenedUrl() throws Exception {
        final Shortened shortened = ShortenedFixture.validShortenedUrl("123abc");
        given(repository.findByShortenedUrlEquals(anyString())).willReturn(shortened);

        final URLShortened urlShortened = service.shortenUrl(shortened.getShortenedUrl());

        verify(repository, never()).save(any(Shortened.class));
        assertThat(urlShortened.getShortenedUrl(), is(shortened.getShortenedUrl()));
    }

    @Test
    public void testShortenDuplicatedUrl() throws Exception {
        final Shortened shortened = ShortenedFixture.validShortenedUrl("123abc");
        given(repository.findByUrlEquals(anyString())).willReturn(shortened);

        final URLShortened urlShortened = service.shortenUrl(shortened.getShortenedUrl());

        verify(repository, never()).save(any(Shortened.class));
        assertThat(urlShortened.getShortenedUrl(), is(shortened.getShortenedUrl()));
    }

    @Test
    public void testGetUrl() throws Exception {
        final String urlCode = "1234ab";
        final Shortened shortened = ShortenedFixture.validShortenedUrl(urlCode);
        given(repository.findByUrlCodeEquals(eq(urlCode))).willReturn(shortened);

        final String url = service.getUrl(urlCode);
        assertThat(url, is(shortened.getUrl()));
    }

    @Test
    public void testGetNonExistentUrl() throws Exception {
        final String urlCode = "abcdef12";
        expectedException.expect(EntityNotFoundException.class);
        expectedException.expectMessage("URL not found for code" + urlCode);

        given(repository.findByUrlCodeEquals(eq(urlCode))).willReturn(null);

        service.getUrl(urlCode);
    }

}