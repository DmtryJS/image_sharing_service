FROM openjdk:8-alpine
LABEL description="Simpe image sharing service. https://github.com/DmtryJS/image_sharing_service"
COPY target/*.jar /usr/src/myapp/app.jar
RUN mkdir /usr/src/myapp/shared
VOLUME /usr/src/myapp/shared
WORKDIR /usr/src/myapp
ENTRYPOINT ["java", "-jar", "app.jar"]