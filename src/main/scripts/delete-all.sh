#!/bin/bash


# Stop and remove all containers
docker ps -aq | while read -r container_id; do
    docker stop "$container_id" >/dev/null 2>&1
    docker rm -f "$container_id" >/dev/null 2>&1
done

# Clean up any remaining containers
docker container prune -f
