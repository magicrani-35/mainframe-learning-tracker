package com.barbarawilliams.model;

import java.time.LocalDate;

public record Challenge(
        String code,
        String title,
        String category,
        String course,
        String status,
        LocalDate startedOn,
        LocalDate completedOn,
        String notes
) {

}