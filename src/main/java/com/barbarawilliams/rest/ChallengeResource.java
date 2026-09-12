package com.barbarawilliams.rest;

import java.util.List;

import com.barbarawilliams.model.Challenge;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@Path("/challenges")
@Produces(MediaType.APPLICATION_JSON)
public class ChallengeResource {

        private final List<Challenge>
            challenges = List.of(
                    new Challenge("JAVA1", "JAVA", "completed"),
                    new Challenge("ASM2", "ASSEMBLER", "completed")
            );

        @GET
        public List<Challenge> getChallenges()
        {
            return challenges;
        }

        @GET
        @Path("/{code}")
        public Challenge
    getChallenge(@PathParam("code") String code)
        {
            return challenges.stream()
                    .filter(challenge ->

                            challenge.code().equalsIgnoreCase(code))
                    .findFirst()
                    .orElseThrow(() -> new WebApplicationException(
                            "Challenge not found: " + code,
                            Response.Status.NOT_FOUND
                    ));
        }

}