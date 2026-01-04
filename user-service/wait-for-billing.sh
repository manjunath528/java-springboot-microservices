#!/bin/sh
set -e

echo "Waiting for billing service to be healthy..."

BILLING_SERVICE_URL="$1"
shift

until curl -f "$BILLING_SERVICE_URL/actuator/health"; do
  echo "Waiting for $BILLING_SERVICE_URL..."
  sleep 5
done

echo "$BILLING_SERVICE_URL is up!"

exec "$@"
