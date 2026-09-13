package com.barbarawilliams.rest;

import java.util.List;

import com.barbarawilliams.model.Challenge;
import com.barbarawilliams.model.ProgressSummary;
import com.barbarawilliams.repository.ChallengeRepository;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/progress")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class ProgressResource {

    @Inject
    ChallengeRepository repository;

    public ProgressResource() {
    }

    ProgressResource(ChallengeRepository repository) {
        this.repository = repository;
    }

    @GET
    public ProgressSummary getProgress() {
        List<Challenge> challenges =
                repository.findAll();

        int total = challenges.size();
        int completed = countStatus(challenges, "completed");
        int inProgress = countStatus(challenges, "in progress");
        int planned = countStatus(challenges, "planned");
        int blocked = countStatus(challenges, "blocked");

        double percentage = total == 0
                ? 0.0
                : completed * 100.0 / total;

        return new ProgressSummary(
                total,
                completed,
                inProgress,
                planned,
                blocked,
                percentage
        );
    }

    private int countStatus(
            List<Challenge> challenges,
            String expectedStatus) {
        return (int) challenges.stream()
                .filter(challenge ->
                        challenge.status() != null
                            && challenge.status()
                                .trim()
                                .equalsIgnoreCase(expectedStatus))
                                .count();
    }
}