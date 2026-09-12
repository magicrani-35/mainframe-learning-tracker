package com.barbarawilliams.rest;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.barbarawilliams.model.Challenge;

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
public class ChallengeResource {

    private static final List<Challenge>
            challenges =
            new CopyOnWriteArrayList<>
                    (List.of(
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

    @GET
    public List<Challenge> getChallenges() {
        return challenges;
    }

    @GET
    @Path("/{code}")
    public Challenge getChallenge(
            @PathParam("code") String code
    ) {
        return challenges.stream()
                .filter(challenge ->
                        challenge.code().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() ->
                        new WebApplicationException(
                                "Challenge not found: " + code,
                                Response.Status.NOT_FOUND
                        )
                );
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

        boolean codeAlreadyExists = challenges.stream()
                .anyMatch(existing ->
                        existing.code().equalsIgnoreCase(challenge.code()
                        )
                );

        if (codeAlreadyExists) {
            throw new WebApplicationException(
                    "Challenge already exists: "
                    + challenge.code(),
                    Response.Status.CONFLICT
            );
        }

        challenges.add(challenge);

        URI challengeUri = uriInfo
                .getAbsolutePathBuilder()
                .path(challenge.code())
                .build();

        return Response.created(challengeUri)
                .entity(challenge)
                .build();
    }
}