FROM openjdk:17-jdk-slim as builder

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY gradlew gradlew.bat gradle/wrapper/ ./
RUN chmod +x ./gradlew

COPY build.gradle gradle.properties settings.gradle ./
COPY gradle gradle/

COPY src src/

RUN ./gradlew build -x test --no-daemon

FROM openjdk:17-jre-slim

RUN groupadd -r appuser && useradd -r -g appuser appuser

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

RUN chown -R appuser:appuser /app
USER appuser

EXPOSE 8099

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
