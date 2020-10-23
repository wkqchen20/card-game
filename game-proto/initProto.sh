#!/bin/bash

SHELL_FOLDER=$(dirname "$0")
cd $SHELL_FOLDER
cd ../
protoc -I=. --java_out=game-common/src/main/java/ game-proto/*.proto
