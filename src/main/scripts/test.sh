#!/bin/bash

JAR_NAME=my-app
JAR_VERSION=1.0

# remove old containers and images
# shellcheck disable=SC2046
docker rm -f $(docker ps -aq)
docker image rm $JAR_NAME:$JAR_VERSION
docker build -t $JAR_NAME:$JAR_VERSION ../../../.

# running it on different terminals
gnome-terminal --geometry=60x15+50+50 --title="Bootstrap Node" -- docker run --name bootstrp -it --rm -e ARG1="true" --ip="172.17.0.2" $JAR_NAME:$JAR_VERSION
gnome-terminal --geometry=60x15+50+395 --title="Node 2" -- docker run -it --rm -e ARG1="false" $JAR_NAME:$JAR_VERSION
gnome-terminal --geometry=60x15+50+740 --title="Node 3" -- docker run -it --rm -e ARG1="false" $JAR_NAME:$JAR_VERSION

sleep 5


# creating some additional containers, just to have a bit bigger network for  testing

#N=10  # Number of containers to create

#for ((i=1; i<=N; i++)); do
#    docker run -it --rm -e ARG1="false" $JAR_NAME:$JAR_VERSION
#done
