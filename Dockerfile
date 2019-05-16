FROM maven:3.6.0-jdk-8-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package
EXPOSE 6569
FROM java:8-jdk-alpine
COPY --from=build /home/app/target/manager-0.0.1-SNAPSHOT.jar /usr/local/lib/manager.jar
COPY --from=build /home/app/src/main/resources/application.properties /usr/local/lib/application.properties
COPY --from=build /home/app/target/dependency-jars /usr/local/lib/dependency-jars
WORKDIR /usr/app
RUN sh -c 'touch manager-0.0.1-SNAPSHOT.jar'
ENTRYPOINT ["java", "-jar", "/usr/local/lib/manager.jar", "--spring.config.location=file:/usr/local/lib/application.properties"]