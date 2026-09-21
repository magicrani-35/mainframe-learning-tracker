package com.barbarawilliams.rest;

import java.util.List;
import java.util.Set;

import com.barbarawilliams.model.Evidence;
import com.barbarawilliams.repository.EvidenceRepository;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/evidence")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class EvidenceResource {

    private static final Set<String> ALLOWED_TYPES = Set.of(
            "JOB",
            "TSO_SESSION",
            "DATA_SET",
            "USS_FILE"
    );

    @Inject
    EvidenceRepository repository;

    public EvidenceResource() {
    }

    EvidenceResource(EvidenceRepository repository) {
        this.repository = repository;
    }

    @GET
    public List<Evidence> getEvidence() {
        return repository.findAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveEvidence(Evidence evidence) {
        validate(evidence);

        Evidence savedEvidence = repository.save(evidence);

        return Response.ok(savedEvidence).build();
    }

    private void validate(Evidence evidence) {
        if (evidence == null) {
            badRequest("Evidence data is required");
        }

        if (isBlank(evidence.evidenceType())) {
            badRequest("Evidence type is required");
        }

        if (!ALLOWED_TYPES.contains(evidence.evidenceType())) {
        badRequest(
                "Unsupported evidence type: "
                + evidence.evidenceType()
            );
        }

        if (isBlank(evidence.sourceSystem())) {
            badRequest("Source system is required");
        }

        if (isBlank(evidence.externalId())) {
            badRequest("External ID is required");
        }

        if (evidence.firstObservedAt() == null) {
            badRequest("First observed timestamp is required");
        }

        if (evidence.lastObservedAt() == null) {
            badRequest("Last observed timestamp is required");
        }

        if (evidence.lastObservedAt()
                .isBefore(evidence.firstObservedAt())) {
            badRequest(
                    "Last observed timestamp cannot be before "
                    + "first observed timestamp"
            );
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private void badRequest(String message) {
        throw new WebApplicationException(
                message,
                Response.Status.BAD_REQUEST
        );
    }
}