#!/usr/bin/env bash

if [ -z "${VERSION}" ]; then
  echo "Please provide a version! (e.g. VERSION=0.0.1 ./deploy-to-docker.sh)"
  exit 1
fi

docker build -t simple-rest-json-server:"${VERSION}" .

docker tag simple-rest-json-server:"${VERSION}" rlindooren/simple-rest-json-server:"${VERSION}"
docker tag simple-rest-json-server:"${VERSION}" rlindooren/simple-rest-json-server:latest

docker push rlindooren/simple-rest-json-server:"${VERSION}"
docker push rlindooren/simple-rest-json-server:latest
