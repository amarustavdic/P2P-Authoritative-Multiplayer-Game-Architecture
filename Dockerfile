FROM openjdk:11
COPY target/prog3-project-1.0-SNAPSHOT-jar-with-dependencies.jar /tmp
WORKDIR /tmp
CMD java -jar prog3-project-1.0-SNAPSHOT-jar-with-dependencies.jar