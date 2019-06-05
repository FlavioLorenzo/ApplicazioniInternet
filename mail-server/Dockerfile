FROM ubuntu:latest

RUN apt-get update -y
RUN apt-get install default-jdk -y
COPY fakeSMTP-2.0.jar fakeSMTP-2.0.jar
EXPOSE 1234

CMD ["java", "-jar", "fakeSMTP-2.0.jar", "-s", "-b", "-p", "1234"]
