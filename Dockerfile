FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -B -q -DskipTests dependency:go-offline

COPY src ./src
RUN mvn -B -DskipTests clean package

FROM eclipse-temurin:17-jre
WORKDIR /app

RUN useradd -r -u 10001 appuser
USER appuser

COPY --from=build /app/target/*.jar /app/app.jar

EXPOSE 8088

ENTRYPOINT ["sh","-c","java -jar /app/app.jar"]
