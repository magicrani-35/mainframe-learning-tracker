#!/usr/bin/env bash

set -euo pipefail

SCRIPT_DIRECTORY="$(
  cd "$(dirname "${BASH_SOURCE[0]}")"
  pwd
)"

PROJECT_ROOT="$(
  cd "$SCRIPT_DIRECTORY/../.."
  pwd
 )"

cd "$PROJECT_ROOT"


if [[ ! -f .env ]]; then
  echo "Missing .env file in $PROJECT_ROOT" >&2
  exit 1
fi

set -a
source .env
set +a

export DB_HOST="${DB_HOST:-localhost}"
export DB_PORT="${DB_PORT:-5433}"

echo "Starting PostgreSQL..."
docker compose up -d database

echo "Database host: $DB_HOST"
echo "Database port: #DB_PORT"
echo "Starting Open Liberty..."

exec ./mvnw liberty:dev