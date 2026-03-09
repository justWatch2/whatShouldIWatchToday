#!/bin/bash
set -e

AWS_REGION=ap-northeast-2
ECR_REGISTRY=__ECR_REGISTRY__

aws ecr get-login-password --region $AWS_REGION | \
  docker login --username AWS --password-stdin $ECR_REGISTRY

cd /opt/app

docker compose pull
