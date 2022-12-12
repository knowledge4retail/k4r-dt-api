FROM maven:3.8-openjdk-17-slim as MAVEN_TOOL_CHAIN

WORKDIR /build

COPY ./k4r-dt-api/src/ /build/k4r-dt-api/src/
COPY ./k4r-dt-api/pom.xml /build/k4r-dt-api/
COPY ./pom.xml /build

RUN mvn --batch-mode --update-snapshots \
      -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=WARN \
      -Dorg.slf4j.simpleLogger.showDateTime=true -Dorg.slf4j.simpleLogger.dateTimeFormat=HH:mm:ss,SSS \
      clean install -P build -pl k4r-dt-api

FROM openjdk:17-alpine
COPY --from=MAVEN_TOOL_CHAIN /build/k4r-dt-api/target/dt-api.jar /usr/app/app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/usr/app/app.jar"]
