#!/usr/bin/env bash

set -euo pipefail

OWNER="${ZOWE_USERNAME:?Set ZOWE_USERNAME before running this script}"
TIMESTAMP="$(date -u +"%Y-%m-%dT%H-%M-%SZ")"

OUTPUT_FILE="${1:-evidence/zowe/raw/jobs-${TIMESTAMP}.json}"

mkdir -p "$(dirname "$OUTPUT_FILE")"

echo "Retrieving recent jobs for $OWNER..."

zowe zos-jobs list jobs \
  --owner "$OWNER" \
  --prefix "*" \
  --response-format-json \
  > "$OUTPUT_FILE"


echo "Saved job history to $OUTPUT_FILE"