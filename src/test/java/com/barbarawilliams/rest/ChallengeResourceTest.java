package com.barbarawilliams.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.barbarawilliams.model.Challenge;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

class ChallengeResourceTest {

    private final ChallengeResource resource =
            new ChallengeResource();

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
}