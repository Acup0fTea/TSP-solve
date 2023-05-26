FROM openjdk:11-jdk

COPY TSPParallelBFS.java /app/TSPParallelBFS.java

WORKDIR /app

RUN javac TSPParallelBFS.java

CMD ["java", "TSPParallelBFS"]
