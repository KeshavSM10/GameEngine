# Use OpenJDK 21 as the base image
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy your JAR file into the container
COPY target/*.jar app.jar

# Expose the port your Spring Boot app runs on (default is 8080)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
