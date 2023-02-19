#!/bin/bash

JAR_NAME=my-app
JAR_VERSION=1.0

# remove old containers and images
# shellcheck disable=SC2046
docker rm -f $(docker ps -aq)
docker image rm $JAR_NAME:$JAR_VERSION
docker build -t $JAR_NAME:$JAR_VERSION ../../../.

# running it on different terminals
gnome-terminal --geometry=60x15+50+50 --title="Bootstrap Node" -- docker run -it --rm -e ARG1="true" $JAR_NAME:$JAR_VERSION
gnome-terminal --geometry=60x15+50+395 --title="Node 2" -- docker run -it --rm -e ARG1="false" $JAR_NAME:$JAR_VERSION
gnome-terminal --geometry=60x15+50+740 --title="Node 3" -- docker run -it --rm -e ARG1="false" $JAR_NAME:$JAR_VERSION
