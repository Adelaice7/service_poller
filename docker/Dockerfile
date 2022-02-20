FROM adoptopenjdk/openjdk11:jdk-11.0.9.1_1
RUN mkdir /service-poller-app
ARG JAR_FILE=../build/service-poller.jar
COPY ${JAR_FILE} /service-poller-app/service-poller.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/service-poller-app/service-poller.jar"]
