package com.barbarawilliams.rest;

import java.net.URI;
import java.util.List;

import com.barbarawilliams.model.Challenge;
import com.barbarawilliams.repository.ChallengeRepository;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("/challenges")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class ChallengeResource {

    @Inject
    ChallengeRepository repository;

    public ChallengeResource() {

    }

    ChallengeResource(ChallengeRepository repository) {
        this.repository = repository;
    }

    @GET
    public List<Challenge> getChallenges() {
        return repository.findAll();
    }

    @GET
    @Path("/{code}")
    public Challenge getChallenge(
            @PathParam("code")
    String code) {
        return repository.findByCode(code)
                .orElseThrow(() -> new WebApplicationException(
                        "Challenge not found: " + code,
                        Response.Status.NOT_FOUND));

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createChallenge(
            Challenge challenge,
            @Context UriInfo uriInfo) {
        if (challenge == null
            || challenge.code() == null
            || challenge.code().isBlank()) {

            throw new WebApplicationException(
                    "Challenge code is required",
                    Response.Status.BAD_REQUEST
            );
        }

        if (repository.codeExists(challenge.code())) {
            throw new WebApplicationException(
                    "Challenge already exists: "
                    + challenge.code(),
                    Response.Status.CONFLICT
            );
        }

        repository.add(challenge);

        URI challengeUri = uriInfo
                .getAbsolutePathBuilder()
                .path(challenge.code())
                .build();

        return Response.created(challengeUri)
                .entity(challenge)
                .build();
    }
}