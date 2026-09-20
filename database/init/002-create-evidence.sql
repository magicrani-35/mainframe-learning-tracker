BEGIN;
CREATE TABLE IF NOT EXISTS evidence (
    id BIGSERIAL PRIMARY KEY,

    challenge_code VARCHAR(40),
    evidence_type VARCHAR(40) NOT NULL,
    source_system VARCHAR(40) NOT NULL,
    external_id VARCHAR(120),
    name VARCHAR(120),
    status VARCHAR(40),
    return_code VARCHAR(40),

    first_observed_at TIMESTAMPTZ NOT NULL,
    last_observed_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT evidence_challenge_fk
        FOREIGN KEY (challenge_code)
        REFERENCES challenges (code)
        ON DELETE SET NULL,

    CONSTRAINT evidence_type_valid
        CHECK (
            evidence_type IN (
            'JOB',
            'TSO_SESSION',
            'DATA_SET',
            'USS_FILE'
            )
        ),

    CONSTRAINT evidence_observation_dates_valid
        CHECK (
            last_observed_at >= first_observed_at
        ),

    CONSTRAINT evidence_source_identity_unique
        UNIQUE (
            source_system,
            evidence_type,
            external_id
        )
);

CREATE INDEX IF NOT EXISTS evidence_challenge_code_index
    ON evidence (challenge_code);

CREATE INDEX IF NOT EXISTS evidence_last_observed_index
    ON evidence (last_observed_at DESC);

COMMIT;