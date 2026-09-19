#!/usr/bin/env python3

import json
import sys
from pathlib import Path

raw_directory = Path("evidence/zowe/raw")
safe_directory = Path("evidence/zowe/safe")

if len(sys.argv) > 1:
    input_path = Path(sys.argv[1])
else:
    snapshots = list(raw_directory.glob("jobs-*.json"))
    
    if not snapshots:
        raise SystemExit(
            "No raw Zowe snapshots found. Run export-job-history.sh first."
        )
        
    input_path = max(
        snapshots,
        key=lambda snapshot: snapshot.stat().st_mtime,
    )
        
timestamped_name = input_path.name.replace(
    "jobs-",
    "evidence-",
    1,
)

output_path = (
    Path(sys.argv[2])
    if len(sys.argv) > 2
    else safe_directory / timestamped_name
)

safe_directory.mkdir(parents=True, exist_ok=True)

with input_path.open(encoding="utf-8") as input_file:
    response = json.load(input_file)

jobs = response.get("data", [])

batch_jobs = [
    {
        "jobName": job.get("jobname"),
        "jobId": job.get("jobid"),
        "status": job.get("status"),
        "returnCode": job.get("retcode"),
    }
    for job in jobs
    if job.get("type") == "JOB"
]

with output_path.open("w", encoding="utf-8") as output_file:
    json.dump(batch_jobs, output_file, indent=2)
    output_file.write("\n")

skipped_count = len(jobs) - len(batch_jobs)

print(f"Read {len(jobs)} JES entries.")
print(f"Exported {len(batch_jobs)} batch jobs.")
print(f"Skipped {skipped_count} non-batch entries")
print(f"Saved safe evidence to {output_path}.")