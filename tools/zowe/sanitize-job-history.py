#!/usr/bin/env python3

import json
import sys
from pathlib import Path

input_path  = Path(
    sys.argv[1] if len(sys.argv) > 1 else "zowe-job-history.json"
)

output_path = Path(
    sys.argv[2] if len(sys.argv) > 2 else "zowe-job-evidence.json"
)

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