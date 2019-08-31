FROM openjdk:8-alpine
COPY target/*.jar /usr/src/myapp/app.jar
RUN mkdir /usr/src/myapp/shared
VOLUME /usr/src/myapp/shared
WORKDIR /usr/src/myapp
ENTRYPOINT ["java", "-jar", "app.jar"]