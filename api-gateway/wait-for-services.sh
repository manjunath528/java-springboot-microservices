#!/bin/sh
set -e

echo "Waiting for services to be healthy..."

services="$@"
for service in $services; do
  until curl -f $service/actuator/health; do
    echo "Waiting for $service..."
    sleep 5
  done
  echo "$service is up!"
done

exec java -jar /app/app.jar
