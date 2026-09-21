package com.barbarawilliams.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.barbarawilliams.model.Evidence;
import com.barbarawilliams.repository.EvidenceRepository;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

class EvidenceResourceTest {

    private EvidenceRepository repository;
    private EvidenceResource resource;
    private Evidence evidence;

    @BeforeEach
    void setUp() {
        repository = mock(EvidenceRepository.class);
        resource = new EvidenceResource(repository);

        evidence = new Evidence(
                1L,
                "ASM2",
                "TSO_SESSION",
                "ZOWE_CLI",
                "TSU05888",
                "Z83410",
                "ACTIVE",
                null,
                OffsetDateTime.parse(
                        "2026-09-16T21:12:00Z"
                ),
                OffsetDateTime.parse("2026-09-16T21:12:00Z")
        );

        when(repository.findAll())
            .thenReturn(List.of(evidence));

        when(repository.save(any(Evidence.class)))
            .thenReturn(evidence);
    }

    @Test
    void returnsAllEvidence() {
        List<Evidence> result = resource.getEvidence();

        assertEquals(1, result.size());
        assertEquals("TSU05888", result.getFirst().externalId());
    }

    @Test
    void savesValidEvidence() {
        Response response = resource.saveEvidence(evidence);

        assertEquals(
                Response.Status.OK.getStatusCode(),
                response.getStatus()
        );

        assertEquals(evidence, response.getEntity());
        verify(repository).save(evidence);
    }

    @Test
    void rejectsUnsupportedEvidenceType() {
        Evidence unsupported = new Evidence(
                null,
                null,
                "SCREENSHOT",
                "MANUAL",
                "image-1",
                "Screenshot",
                "CAPTURED",
                null,
                evidence.firstObservedAt(),
                evidence.lastObservedAt()
        );

        WebApplicationException exception =
                assertThrows(
                        WebApplicationException.class,
                        () -> resource.saveEvidence(unsupported)
                );
        assertEquals(
                Response.Status.BAD_REQUEST.getStatusCode(),
                exception.getResponse().getStatus()
        );
    }

    @Test
    void rejectsReversedObservationDates() {
        Evidence reversedDates = new Evidence(
                null,
                null,
                "JOB",
                "ZOWE_CLI",
                "JOB12345",
                "TESTJOB",
                "OUTPUT",
                "CC 0000",
                OffsetDateTime.parse(
                        "2026-09-16T22:00:00Z"
                ),
                OffsetDateTime.parse("2026-09-16T21:00:00Z")
        );

        WebApplicationException exception =
                assertThrows(
                        WebApplicationException.class,
                        () -> resource.saveEvidence(reversedDates)
                );
        assertEquals(
                Response.Status.BAD_REQUEST.getStatusCode(),
                exception.getResponse().getStatus()
        );
    }
}