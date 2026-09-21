 package com.barbarawilliams.model;

import java.time.OffsetDateTime;

public record Evidence(
        Long id,
        String challengeCode,
        String evidenceType,
        String sourceSystem,
        String externalId,
        String name,
        String status,
        String returnCode,
        OffsetDateTime firstObservedAt,
        OffsetDateTime lastObservedAt
) {
}