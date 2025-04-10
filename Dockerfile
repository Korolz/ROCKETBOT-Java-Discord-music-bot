FROM openjdk:21
LABEL maintainer="korolz"
WORKDIR /rocketbot
COPY ./build/libs/ /rocketbot
CMD ["java", "-jar", "ROCKETBOT-1.0.5.jar"]
