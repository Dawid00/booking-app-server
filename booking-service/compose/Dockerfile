FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY booking-service-0.0.1-SNAPSHOT.jar app.jar
SHELL ["/bin/bash", "-c"]
CMD "java -jar ./build/libs/app.jar"