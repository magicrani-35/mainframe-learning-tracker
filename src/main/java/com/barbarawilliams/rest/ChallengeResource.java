package com.barbarawilliams.rest;

import java.util.List;

import com.barbarawilliams.model.Challenge;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/challenges")
@Produces(MediaType.APPLICATION_JSON)
public class ChallengeResource {

        @GET
        public List<Challenge> getChallenges() {
            return List.of(
                    new Challenge("JAVA1", "JAVA", "completed"),
                    new Challenge("ASM2", "ASSEMBLER", "completed")
            );
        }
}