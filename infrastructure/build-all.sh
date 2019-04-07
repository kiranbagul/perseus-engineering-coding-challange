#!/usr/bin/env bash

cd ../user-service
./ci-scripts/build.sh

cd ../template-service
./ci-scripts/build.sh

cd ../notification-service
./ci-scripts/build.sh
