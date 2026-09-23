package com.barbarawilliams.repository;

import java.util.List;
import java.util.Optional;

import com.barbarawilliams.model.Evidence;
import com.barbarawilliams.persistence.EvidenceEntity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class EvidenceRepository {

    @PersistenceContext(unitName = "learningTracker")
    EntityManager entityManager;

    public List<Evidence> findAll() {
        return entityManager
                .createQuery(
                        """
                                SELECT evidence
                                FROM EvidenceEntity evidence
                                ORDER BY evidence.lastObservedAt DESC
                                """,
                                EvidenceEntity.class
                )
                .getResultStream()
                .map(EvidenceEntity::toModel)
                .toList();
    }

    @Transactional
    public  Evidence save(Evidence evidence) {
        Optional<EvidenceEntity> existing =
                findBySourceIdentity(
                        evidence.sourceSystem(),
                        evidence.evidenceType(),
                        evidence.externalId()
                );

        if (existing.isPresent()) {
            EvidenceEntity entity = existing.get();
            entity.updateObservation(evidence);
            return entity.toModel();
        }

        EvidenceEntity entity = new EvidenceEntity(evidence);
        entityManager.persist(entity);
        entityManager.flush();

        return entity.toModel();
    }

    private Optional<EvidenceEntity> findBySourceIdentity(
            String sourceSystem,
            String evidenceType,
            String externalId) {

        if (externalId == null) {
            return Optional.empty();
        }

        return entityManager
                .createQuery(
                        """
                                SELECT evidence
                                FROM EvidenceEntity evidence
                                WHERE evidence.sourceSystem =
                                    :sourceSystem
                                AND evidence.evidenceType =
                                    :evidenceType
                                AND evidence.externalId = :externalId
                                """,
                                EvidenceEntity.class
                )
                .setParameter("sourceSystem", sourceSystem)
                .setParameter("evidenceType", evidenceType)
                .setParameter("externalId", externalId)
                .getResultStream()
                .findFirst();
    }
}