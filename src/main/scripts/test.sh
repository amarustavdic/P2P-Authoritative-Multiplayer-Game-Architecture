#!/bin/bash

JAR_NAME=my-app
JAR_VERSION=1.0

# Remove old containers and images
docker rm -f $(docker ps -aq)
docker image rm $JAR_NAME:$JAR_VERSION

# Build the Docker image
docker build -t $JAR_NAME:$JAR_VERSION ../../../.

# Running containers in different terminals
gnome-terminal --geometry=60x15+50+50 --title="Bootstrap Node" -- docker run --name bootstrp -it --rm -e ARG1="true" --ip="172.17.0.2" -e DISPLAY=$DISPLAY -v /tmp/.X11-unix:/tmp/.X11-unix:ro $JAR_NAME:$JAR_VERSION
gnome-terminal --geometry=60x15+50+395 --title="Node 2" -- docker run -it --rm -e ARG1="false" -e DISPLAY=$DISPLAY -v /tmp/.X11-unix:/tmp/.X11-unix:ro $JAR_NAME:$JAR_VERSION
gnome-terminal --geometry=60x15+50+740 --title="Node 3" -- docker run -it --rm -e ARG1="false" -e DISPLAY=$DISPLAY -v /tmp/.X11-unix:/tmp/.X11-unix:ro $JAR_NAME:$JAR_VERSION

