package com.barbarawilliams.rest;

import java.util.List;
import java.util.Map;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/challenges")
@Produces(MediaType.APPLICATION_JSON)
public class ChallengeResource {

        @GET
        public List<Map<String, String>>
    getChallenges() {
            return List.of(
                    Map.of(
                            "code", "JAVA1",
                            "category", "JAVA",
                            "status", "in progress"
                    )
            );
        }
}