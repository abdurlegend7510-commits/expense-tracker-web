# ---- Build stage: compiles the project with Maven ----
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# ---- Run stage: just the JRE + the built jar, kept small ----
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/expense-tracker-web.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
