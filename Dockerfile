FROM amazoncorretto:11-alpine-jdk
ARG JAR_FILE=build/libs/moneyTransfer-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} server.jar
ENTRYPOINT ["java","-jar","server.jar"]