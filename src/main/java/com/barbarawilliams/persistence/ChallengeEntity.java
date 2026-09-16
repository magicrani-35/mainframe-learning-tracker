package com.barbarawilliams.persistence;

import java.time.LocalDate;

import com.barbarawilliams.model.Challenge;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "challenges")
public class ChallengeEntity {

    @Id
    @Column(length = 40)
    private String code;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 80)
    private String category;

    @Column(nullable = false, length = 200)
    private String course;

    @Column(nullable = false, length = 30)
    private String status;

    @Column(name = "started_on")
    private LocalDate startedOn;

    @Column(name = "completed_on")
    private LocalDate completedOn;

    @Column
    private String notes;

    protected ChallengeEntity() {
        // Required by Jakarta Persistence.
    }

    public ChallengeEntity(Challenge challenge) {
        this.code = challenge.code();
        updateFrom(challenge);
    }

    public void updateFrom(Challenge challenge) {
        this.title = challenge.title();
        this.category = challenge.category();
        this.course = challenge.course();
        this.status = challenge.status();
        this.startedOn = challenge.startedOn();
        this.completedOn = challenge.completedOn();
        this.notes = challenge.notes();
    }

    public Challenge toModel() {
        return new Challenge(
                code,
                title,
                category,
                course,
                status,
                startedOn,
                completedOn,
                notes
        );
    }
}