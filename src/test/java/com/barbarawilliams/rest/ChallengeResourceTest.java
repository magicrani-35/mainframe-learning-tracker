package com.barbarawilliams.rest;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.time.LocalDate;

import com.barbarawilliams.repository.ChallengeRepository;
import org.junit.jupiter.api.Test;

import com.barbarawilliams.model.Challenge;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;

class ChallengeResourceTest {

    private ChallengeResource resource;

    @BeforeEach
    void setUp() {
        ChallengeRepository repository =
                new ChallengeRepository();

        resource = new ChallengeResource(repository);
    }

    @Test
    void returnsAllChallenges() {
        assertEquals(2, resource.getChallenges().size());
    }

    @Test
    void returnsChallengeByCode() {
        Challenge challenge = resource.getChallenge("JAVA1");

        assertEquals("JAVA1", challenge.code());
        assertEquals("JAVA", challenge.category());
        assertEquals("completed", challenge.status());
    }

    @Test
    void codeSearchIgnoresLetterCase() {
        Challenge challenge = resource.getChallenge("asm2");

        assertEquals("ASM2", challenge.code());
    }

    @Test
    void missingChallengeReturnsNotFound() {
        WebApplicationException exception =
                assertThrows(
                        WebApplicationException.class,
                        () -> resource.getChallenge("DOES-NOT-EXIST")
                );

        assertEquals(
                Response.Status.NOT_FOUND.getStatusCode(),
                exception.getResponse().getStatus()
        );
    }

    @Test
    void createChallenge() {
        UriInfo uriInfo = mock(UriInfo.class);

        when(uriInfo.getAbsolutePathBuilder())
                .thenReturn(
                        UriBuilder.fromUri(
                                "http://localhost/api/challenges"
                        )
                );

        Challenge challenge =
                new Challenge(
                        "USS2-TEST",
                        "USS Part 2",
                        "USS",
                        "IBM Z Xplore",
                        "completed",
                        LocalDate.of(2026, 9, 8),
                        LocalDate.of(2026, 9, 8),
                        "Practiced shell scripting"
                );

        Response response =
                resource.createChallenge(challenge, uriInfo);
        assertEquals(
                Response.Status.CREATED.getStatusCode(),
                response.getStatus()
        );

        assertEquals(challenge, response.getEntity());

        assertEquals(
                URI.create(
                        "http://localhost/api/challenges/USS2-TEST"
                ),
                response.getLocation()
        );
    }

    @Test
    void rejectMissingChallengeCode() {
        Challenge challenge =
                new Challenge(
                        " ",
                        "Test Challenge",
                        "USS",
                        "IBM Z Xplore",
                        "in progress",
                        null,
                        null,
                        null
                );

        WebApplicationException exception =
                assertThrows(
                        WebApplicationException.class,
                        () -> resource.createChallenge(
                                challenge,
                                null
                        )
                );

        assertEquals(
                Response.Status.BAD_REQUEST.getStatusCode(),
                exception.getResponse().getStatus()
        );
    }

    @Test
    void rejectDuplicateChallengeCode() {
        Challenge duplicate =
                new Challenge(
                        "java1",
                        "Duplicate Java Challenge",
                        "JAVA",
                        "IBM Z Xplore",
                        "completed",
                        null,
                        null,
                        null
                );

        WebApplicationException exception =
                assertThrows(
                        WebApplicationException.class,
                        () -> resource.createChallenge(
                                duplicate,
                                null
                        )
                );

        assertEquals(
                Response.Status.CONFLICT.getStatusCode(),
                exception.getResponse().getStatus()
        );
    }
}