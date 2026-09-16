package com.barbarawilliams.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.barbarawilliams.model.Challenge;
import com.barbarawilliams.model.ProgressSummary;
import com.barbarawilliams.repository.ChallengeRepository;

class ProgressResourceTest {

    @Test
    void calculateProgressSummary() {
        ChallengeRepository repository =
                mock(ChallengeRepository.class);

        when(repository.findAll()).thenReturn(
                List.of(
                        challenge("JAVA1", "completed"),
                        challenge("ASM2", "completed"),
                        challenge("CURRENT", "in progress"),
                        challenge("PLANNED", "planned"),
                        challenge("BLOCKED", "blocked")
                )
        );

        ProgressResource resource =
                new ProgressResource(repository);

        ProgressSummary summary =
                resource.getProgress();

        assertEquals(5, summary.total());
        assertEquals(2, summary.completed());
        assertEquals(1, summary.inProgress());
        assertEquals(1, summary.planned());
        assertEquals(1, summary.blocked());
        assertEquals(
                40.0,
                summary.completionPercentage()
        );
    }

    private Challenge challenge(
            String code,
            String status) {
        return new Challenge(
                code,
                code + " challenge",
                "TEST",
                "Test course",
                status,
                null,
                null,
                null
        );
    }
}