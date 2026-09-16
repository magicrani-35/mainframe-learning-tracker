#!/usr/bin/env bash

set -euo pipefail

OUTPUT_FILE="${1:-zowe-job-history.json}"
OWNER="${ZOWE_USERNAME:?Set ZOWE_USERNAME before running this script}"

echo "Retrieving recent jobs for $OWNER..."

zowe zos-jobs list jobs \
  --owner "$OWNER" \
  --prefix "*" \
  --response-format-json \
  > "$OUTPUT_FILE"


echo "Saved job history to $OUTPUT_FILE"