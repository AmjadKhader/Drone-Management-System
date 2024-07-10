# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim-buster

# Set the working directory in the container
WORKDIR /app

# Copy the executable JAR file to the container
COPY target/management-service-1.0.0.jar /app/management-service.jar

# Expose the port the application runs on
EXPOSE 8092

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "management-service.jar"]
