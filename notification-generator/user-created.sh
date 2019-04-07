#!/usr/bin/env bash

USER=$1
curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"ID":"'${USER}'"}' \
  http://localhost:9003/v1/notifications