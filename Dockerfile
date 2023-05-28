FROM openjdk:8-jdk-alpine

WORKDIR /app

COPY Server.java /app/Server.java
COPY Client.java /app/Client.java

RUN javac Server.java
RUN javac Client.java

EXPOSE 8080-8084

ENTRYPOINT ["java", "Server"]

