#!/usr/bin/env bash

set -e

mvn clean install -DskipTests

./build-native-config.sh

native-image --report-unsupported-elements-at-runtime \
  --no-fallback  \
  --verbose  \
  --enable-http  \
  -H:ResourceConfigurationFiles=target/graal-cfg/resource-config.json \
  -H:ReflectionConfigurationFiles=target/graal-cfg/reflect-config.json \
  -jar target/config-processor-0.0.2-SNAPSHOT-jar-with-dependencies.jar config-processor

chmod u+x config-processor

time ./test-processor-native.sh
