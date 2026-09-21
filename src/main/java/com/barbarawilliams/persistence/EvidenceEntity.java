package com.barbarawilliams.persistence;

import java.time.OffsetDateTime;

import com.barbarawilliams.model.Evidence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "evidence")
public class EvidenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "challenge_code", length = 40)
    private String challengeCode;

    @Column(name = "evidence_type", nullable = false, length = 40)
    private String evidenceType;

    @Column(name = "source_system", nullable = false, length = 40)
    private String sourceSystem;

    @Column(name = "external_id", length = 120)
    private String externalId;

    @Column(length = 120)
    private String name;

    @Column(length = 40)
    private String status;

    @Column(name = "return_code", length = 40)
    private String returnCode;

    @Column(name = "first_observed_at", nullable = false)
    private OffsetDateTime firstObservedAt;

    @Column(name = "last_observed_at", nullable = false)
    private OffsetDateTime lastObservedAt;

    protected EvidenceEntity() {
        // Required by Jakarta Persistence.
    }

    public EvidenceEntity(Evidence evidence) {
        this.challengeCode = evidence.challengeCode();
        this.evidenceType = evidence.evidenceType();
        this.sourceSystem = evidence.sourceSystem();
        this.externalId = evidence.externalId();
        this.name = evidence.name();
        this.status = evidence.status();
        this.returnCode = evidence.returnCode();
        this.firstObservedAt = evidence.firstObservedAt();
        this.lastObservedAt = evidence.lastObservedAt();
    }

    public void updateObservation(Evidence evidence) {
        if (evidence.challengeCode() != null) {
            this.challengeCode = evidence.challengeCode();
        }

        this.name = evidence.name();
        this.status = evidence.status();
        this.returnCode = evidence.returnCode();

        if (firstObservedAt == null
        ||
        evidence.firstObservedAt().isBefore(firstObservedAt)) {
            firstObservedAt = evidence.firstObservedAt();
        }

        if (lastObservedAt == null
        ||
        evidence.lastObservedAt().isAfter(lastObservedAt)) {
            lastObservedAt = evidence.lastObservedAt();
        }
    }

    public Evidence toModel() {
        return new Evidence(
                id,
                challengeCode,
                evidenceType,
                sourceSystem,
                externalId,
                name,
                status,
                returnCode,
                firstObservedAt,
                lastObservedAt
        );
    }
}