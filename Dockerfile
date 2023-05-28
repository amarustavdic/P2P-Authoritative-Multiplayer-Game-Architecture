FROM ubuntu:latest

RUN apt-get update && apt-get install -y openjdk-19-jdk

COPY target/app-1.0-SNAPSHOT-jar-with-dependencies.jar /tmp
WORKDIR /tmp

CMD java -jar app-1.0-SNAPSHOT-jar-with-dependencies.jar