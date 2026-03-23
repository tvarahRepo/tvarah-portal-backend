# --- Build stage ---
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN apk add --no-cache maven && mvn -q clean package -DskipTests

# --- Runtime stage ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

RUN addgroup -S tvarah && adduser -S tvarah -G tvarah

COPY --from=builder /app/target/*.jar app.jar

RUN chown tvarah:tvarah app.jar
USER tvarah

EXPOSE 8201

ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-jar", "app.jar"]
