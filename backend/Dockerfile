FROM ubuntu:latest

RUN apt-get update -y
RUN apt-get install openjdk-8-jdk -y

#COPY target/backend-0.0.1-SNAPSHOT.jar /var/www/spring-boot-application.jar
EXPOSE 8080

CMD ["java", "-jar", "/var/www/exercise_3-0.0.1-SNAPSHOT.jar"]
