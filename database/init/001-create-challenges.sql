CREATE TABLE IF NOT EXISTS challenges (
    code VARCHAR(40) PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    category VARCHAR(80) NOT NULL,
    course VARCHAR(200) NOT NULL,
    status VARCHAR(30) NOT NULL,
    started_on DATE,
    completed_on DATE,
    notes TEXT,

    CONSTRAINT challenge_status_valid
    CHECK (
        status IN (
            'planned',
            'in progress',
            'completed',
            'blocked'
                  )
          ),

    CONSTRAINT challenge_dates_valid
    CHECK (
        completed_on IS NULL
          OR started_on IS NULL
          OR completed_on >= started_on
          )
);

INSERT INTO challenges (
                        code,
                        title,
                        category,
                        course,
                        status,
                        started_on,
                        completed_on,
                        notes
)
VALUES
    (
     'JAVA1',
     'Java on z/OS',
     'JAVA',
     'IBM Z Xplore',
     'completed',
     NULL,
     DATE '2026-09-11',
     'Complied and ran Java programs in USS.'
    ),
    (
     'ASM2',
     'Assembler Part 2',
     'ASSEMBLER',
     'IBM Z Xplore',
     'completed',
     NULL,
     DATE '2026-09-11',
     'Used TSO TEST and worked with assembler load modules.'
    )
ON CONFLICT (code) DO NOTHING;