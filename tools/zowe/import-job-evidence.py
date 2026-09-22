#!/usr/bin/env python3

import json
import os
import sys
from datetime import datetime, timezone
from pathlib import Path
from urllib.error import HTTPError, URLError
from urllib.request import Request, urlopen

SAFE_DIRECTORY = Path("evidence/zowe/safe")

API_URL = os.getenv(
    "EVIDENCE_API_URL",
    "http://localhost:9080/"
    "mainframe-learning-tracker/api/evidence",
)

CHALLENGE_CODE = os.getenv("CHALLENGE_CODE")


def find_input_file() -> Path:
    if len(sys.argv) > 1:
        return Path(sys.argv[1])

    snapshots = list(SAFE_DIRECTORY.glob("evidence-*.json"))

    if not snapshots:
        raise SystemExit(
            "No sanitized evidence snapshots found."
            "Run sanitize-job-evidence.py first."
        )

    return max(
        snapshots,
        key=lambda snapshot: snapshot.stat().st_mtime,
    )


def observed_timestamp(input_path: Path) -> str:
    timestamp_text = (
        input_path.stem
        .removeprefix("evidence-")
    )

    try:
        observed = datetime.strptime(
            timestamp_text,
            "%Y-%m-%dT%H-%M-%SZ",
        ).replace(tzinfo=timezone.utc)
        return observed.isoformat().replace(
            "+00:00",
            "Z",
        )

    except ValueError:
        return datetime.now(timezone.utc).isoformat().replace(
            "+00:00",
            "Z",
        )


def post_evidence(payload: dict) -> dict:
    request = Request(
        API_URL,
        data=json.dumps(payload).encode("utf-8"),
        headers={"Content-Type": "application/json"},
        method="POST",
    )

    with urlopen(request) as response:
        return json.load(response)


input_path = find_input_file()
observed_at = observed_timestamp(input_path)

with input_path.open(encoding="utf-8") as input_file:
    jobs = json.load(input_file)

imported_count = 0

for job in jobs:
    payload = {
        "challengeCode": CHALLENGE_CODE,
        "evidenceType": "JOB",
        "sourceSystem": "ZOWE_CLI",
        "externalId": job["jobId"],
        "name": job["jobName"],
        "status": job["status"],
        "returnCode": job.get("returnCode"),
        "firstObservedAt": observed_at,
        "lastObservedAt": observed_at,
    }

    try: 
        saved = post_evidence(payload)
    except HTTPError as error:
        details = error.read().decode("utf-8")
        raise SystemExit(
            f"API rejected {job['jobId']}: "
            f"{error.code} {details}"
        ) from error
    except URLError as error:
        raise SystemExit(
            "Could not reach the evidence API. "
            "Make sure PostgreSQL and Liberty are running."
        ) from error

    print(
        f"Imported {saved['externalId']} "
        f"as evidence ID {saved['id']}."
    )

    imported_count += 1

saved_id = saved.get("id", "pending")

print(
    f"Read {len(jobs)} sanitized jobs. "
    f"Imported {imported_count} evidence records."
)