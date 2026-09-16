package com.barbarawilliams.repository;

import java.util.List;
import java.util.Optional;

import com.barbarawilliams.model.Challenge;
import com.barbarawilliams.persistence.ChallengeEntity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ChallengeRepository {

    @PersistenceContext(unitName = "learningTracker")
    EntityManager entityManager;

    public List<Challenge> findAll() {
        return entityManager
                .createQuery(
                        """
                SELECT challenge
                FROM ChallengeEntity challenge
                ORDER BY challenge.code
                """,
                        ChallengeEntity.class
                )
                .getResultStream()
                .map(ChallengeEntity::toModel)
                .toList();
    }

    public Optional<Challenge> findByCode(String code) {
        return findEntityByCode(code)
                .map(ChallengeEntity::toModel);
    }

    public boolean codeExists(String code) {
        return findEntityByCode(code).isPresent();
    }

    @Transactional
    public void add(Challenge challenge) {
        entityManager.persist(
                new ChallengeEntity(challenge)
        );
    }

    @Transactional
    public Optional<Challenge> update(
            String code,
            Challenge updatedChallenge) {

        Optional<ChallengeEntity> existing =
                findEntityByCode(code);

        if (existing.isEmpty()) {
            return Optional.empty();
        }

        ChallengeEntity entity = existing.get();
        entity.updateFrom(updatedChallenge);

        return Optional.of(entity.toModel());
    }

    private Optional<ChallengeEntity> findEntityByCode(String code) {
        return entityManager
                .createQuery(
                        """
                                SELECT challenge
                                FROM ChallengeEntity challenge
                                WHERE UPPER(challenge.code) =
                                UPPER(:code)
                                """,
                        ChallengeEntity.class
                )
                .setParameter("code", code)
                .getResultStream()
                .findFirst();
    }
}