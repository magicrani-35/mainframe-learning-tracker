package com.barbarawilliams.rest;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.barbarawilliams.repository.ChallengeRepository;
import org.junit.jupiter.api.Test;

import com.barbarawilliams.model.Challenge;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;

class ChallengeResourceTest {

    private ChallengeResource resource;
    private ChallengeRepository repository;
    private Challenge javaChallenge;
    private Challenge asmChallenge;

    @BeforeEach
    void setUp() {
        repository = mock(ChallengeRepository.class);

        javaChallenge =
                new Challenge(
                        "JAVA1",
                        "Java on z/OS",
                        "JAVA",
                        "IBM Z Xplore",
                        "completed",
                        null,
                        LocalDate.of(2026, 9, 11),
                        "Compiled and ran Java programs in USS."
                );
        asmChallenge =
                new Challenge(
                        "ASM2",
                        "Assembler Part 2",
                        "ASSEMBLER",
                        "IBM Z Xplore",
                        "completed",
                        null,
                        LocalDate.of(2026, 9, 11),
                        "Used TSO TEST and worked with assembler."
                );

        when(repository.findAll())
                .thenReturn(List.of(javaChallenge, asmChallenge));

        when(repository.findByCode("JAVA1"))
                .thenReturn(Optional.of(javaChallenge));

        when(repository.findByCode("asm2"))
                .thenReturn(Optional.of(asmChallenge));

        when(repository.findByCode("DOES-NOT-EXIST"))
                .thenReturn(Optional.empty());

        when(repository.codeExists("java1"))
                .thenReturn(true);

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
        verify(repository).add(challenge);

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

    @Test
    void updateExistingChallenge() {
        Challenge request =
                new Challenge(
                        "ignored-code",
                        "Updated Java Challenge",
                        "JAVA",
                        "IBM Z Xplore",
                        "in progress",
                        LocalDate.of(2026, 9, 15),
                        null,
                        "Reviewing Java and REST APIs"
                );

        Challenge expected =
                new Challenge(
                        "JAVA1",
                        request.title(),
                        request.category(),
                        request.course(),
                        request.status(),
                        request.startedOn(),
                        request.completedOn(),
                        request.notes()
                );
        when(repository.update("java1", expected))
                .thenReturn(Optional.of(expected));

        Challenge result =
                resource.updateChallenge("java1", request);

        assertEquals("JAVA1", result.code());
        assertEquals("Updated Java Challenge", result.title());
        assertEquals("in progress", result.status());
        assertEquals(
                "Reviewing Java and REST APIs",
                result.notes()
        );

        verify(repository).update("java1", expected);
    }

    @Test
    void updateMissingChallengeReturnsNotFound() {
        Challenge update =
                new Challenge(
                        "MISSING",
                        "Missing Challenge",
                        "JAVA",
                        "IBM Z Xplore",
                        "planned",
                        null,
                        null,
                        null
                );

        when(repository.update(
                eq("MISSING"),
                any(Challenge.class)
        )).thenReturn(Optional.empty());

        WebApplicationException exception =
                assertThrows(
                        WebApplicationException.class,
                        () -> resource.updateChallenge("MISSING",
                                update
                        )
                );

        assertEquals(
                Response.Status.NOT_FOUND.getStatusCode(),
                exception.getResponse().getStatus()
        );
    }
}