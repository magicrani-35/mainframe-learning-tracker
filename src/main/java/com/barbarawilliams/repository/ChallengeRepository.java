package com.barbarawilliams.repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import com.barbarawilliams.model.Challenge;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ChallengeRepository {

    private final List<Challenge> challenges =
            new CopyOnWriteArrayList<>(List.of(
                    new Challenge(
                            "JAVA1",
                            "JAVA",
                            "completed"
                    ),
                    new Challenge(
                            "ASM2",
                            "ASSEMBLER",
                            "completed"
                    )
            ));

    public List<Challenge> findAll() {
        return List.copyOf(challenges);
    }

    public Optional<Challenge> findByCode(String code) {
        return challenges.stream()
                .filter(challenge ->

                        challenge.code().equalsIgnoreCase(code))
                            .findFirst();
    }

    public boolean codeExists(String code) {
        return findByCode(code).isPresent();
    }

    public void add(Challenge challenge) {
        challenges.add(challenge);
    }
}