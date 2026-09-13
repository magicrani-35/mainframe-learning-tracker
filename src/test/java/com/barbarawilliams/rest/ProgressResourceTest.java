package com.barbarawilliams.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.barbarawilliams.model.Challenge;
import com.barbarawilliams.model.ProgressSummary;
import com.barbarawilliams.repository.ChallengeRepository;

class ProgressResourceTest {

    @Test
    void calculateProgressSummary() {
        ChallengeRepository repository =
                new ChallengeRepository();

        repository.add(new Challenge(
                "CURRENT",
                "Current project",
                "JAVA",
                "Independent project",
                "in progress",
                null,
                null,
                null
        ));

        repository.add(new Challenge(
                "PLANNED",
                "Future challenge",
                "COBOL",
                "Independent project",
                "planned",
                null,
                null,
                null
        ));

        repository.add(new Challenge(
                "BLOCKED",
                "Blocked challenge",
                "JCL",
                "Independent project",
                "blocked",
                null,
                null,
                null
        ));

        ProgressResource resource =
                new ProgressResource(repository);

        ProgressSummary summary =
                resource.getProgress();

        assertEquals(5, summary.total());
        assertEquals(2, summary.completed());
        assertEquals(1, summary.inProgress());
        assertEquals(1, summary.planned());
        assertEquals(1, summary.blocked());
        assertEquals(40.0, summary.completionPercentage());
    }
}