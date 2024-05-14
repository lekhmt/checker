FROM ubuntu:22.04
VOLUME /tmp
COPY build/libs/checker-0.0.1-SNAPSHOT.jar /app.jar
RUN apt-get -y -qq update && apt-get -y -qq install g++ && apt-get -y install default-jre && g++ --version && java --version
ENTRYPOINT ["java", "-jar", "/app.jar"]