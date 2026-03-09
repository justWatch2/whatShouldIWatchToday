#!/bin/bash
set -e

cd /opt/app

if [ -f docker-compose.yml ]; then
  docker compose down || true
fi
