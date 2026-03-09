#!/bin/bash
set -e

curl -f http://localhost/api/health || exit 1
