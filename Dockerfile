FROM openjdk:11-jdk

WORKDIR /app

COPY TSPSolution.java /app

RUN javac TSPSolution.java

CMD ["java", "TSPSolution"]
