#!/usr/bin/env bash
sbt assembly
docker build . -t template-service