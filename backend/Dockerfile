# Build Stage - use Maven with Java 21
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy pom.xml first to download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the source code
COPY src ./src

# Build Spring Boot app (PortalApplication.java is inside src/main/java)
RUN mvn clean package -DskipTests

# Run Stage - only JRE needed
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy jar file from builder
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
