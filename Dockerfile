ARG SERVICE=account-service

# ── Build stage ────────────────────────────────────────────────────────────────
FROM eclipse-temurin:26-jdk AS builder
ARG SERVICE
WORKDIR /workspace

COPY .mvn/ .mvn/
COPY mvnw ./
RUN chmod +x mvnw

# Copy all POMs first so dependency downloads are cached in a separate layer
COPY pom.xml ./
COPY shared/pom.xml shared/pom.xml
COPY account-service/pom.xml account-service/pom.xml
COPY subscription-service/pom.xml subscription-service/pom.xml
COPY transaction-service/pom.xml transaction-service/pom.xml
COPY favorite-service/pom.xml favorite-service/pom.xml
COPY playlist-service/pom.xml playlist-service/pom.xml
COPY api-gateway/pom.xml api-gateway/pom.xml

RUN ./mvnw -pl ${SERVICE} --also-make dependency:go-offline -q || true

# Copy full source and build the target service
COPY . .
RUN ./mvnw -pl ${SERVICE} --also-make package -DskipTests -q

# ── Runtime stage ──────────────────────────────────────────────────────────────
FROM eclipse-temurin:26-jdk
ARG SERVICE
WORKDIR /app

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

COPY --from=builder /workspace/${SERVICE}/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
