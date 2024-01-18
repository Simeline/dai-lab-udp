# Use an OpenJDK runtime as a base image
FROM openjdk:11-jre-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container at /app
COPY Musician.jar /app/app.jar

# Specify the command to run on container startup
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

