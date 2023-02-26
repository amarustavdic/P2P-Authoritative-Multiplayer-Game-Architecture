FROM openjdk:11
COPY target/app-1.0-SNAPSHOT-jar-with-dependencies.jar /tmp
WORKDIR /tmp
CMD java -jar app-1.0-SNAPSHOT-jar-with-dependencies.jar