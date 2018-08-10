#!/bin/bash


function runPublish {
  ./gradlew ":gradle-scalastyle-plugin_${1}:publishPlugins"

  if [ $? -ne 0 ]; then
    echo "Publishing failed for ${1}"
    exit $?
  fi
}

runPublish "2.10"
runPublish "2.11"
runPublish "2.12"

echo "All is done."
