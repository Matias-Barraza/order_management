FROM eclipse-temurin:21-jdk AS builder

LABEL maintainer="Matias Barraza"

WORKDIR /app

COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

# Grant execution permissions to the MVNW script to enable compilation.
RUN chmod +x ./mvnw

RUN ./mvnw dependency:go-offline

COPY src src

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Crete user no-root for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=builder /app/target/*.jar ./order-management-api.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "order-management-api.jar"]






