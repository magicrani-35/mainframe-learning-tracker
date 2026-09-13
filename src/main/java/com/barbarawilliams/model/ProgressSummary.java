package com.barbarawilliams.model;

public record ProgressSummary(
        int total,
        int completed,
        int inProgress,
        int planned,
        int blocked,
        double completionPercentage
) {

}