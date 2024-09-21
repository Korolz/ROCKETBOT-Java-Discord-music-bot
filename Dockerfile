FROM openjdk:21
LABEL authors="korolz"
COPY build/libs/ROCKETBOT-1.0.2.jar .
CMD ["java", "-jar", "ROCKETBOT-1.0.2.jar"]