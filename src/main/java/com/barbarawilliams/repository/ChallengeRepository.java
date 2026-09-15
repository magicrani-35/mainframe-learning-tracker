package com.barbarawilliams.repository;

import java.time.LocalDate;
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
                            "Java on z/OS",
                            "JAVA",
                            "IBM Z Xplore",
                            "completed",
                            null,
                            LocalDate.of(2026, 9, 11),
                            "Complied and ran Java programs in USS."
                    ),
                    new Challenge(
                            "ASM2",
                            "Assembler Part 2",
                            "ASSEMBLER",
                            "IBM Z Xplore",
                            "completed",
                            null,
                            LocalDate.of(2026, 9, 11),
                            "Used TSO TEST and worked with assembler load modules."
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

    public Optional<Challenge> update(
            String code,
            Challenge updatedChallenge) {
        for (int index = 0; index < challenges.size(); index++) {
            Challenge existingChallenge = challenges.get(index);

            if (existingChallenge.code().equalsIgnoreCase(code)) {
                challenges.set(index,  updatedChallenge);
                return Optional.of(updatedChallenge);
            }
        }

        return Optional.empty();
    }
}