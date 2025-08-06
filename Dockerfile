# Use a base image with Java installed
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy your compiled JAR file into the container
COPY target/open-currentWeatherDataImpl-map-bot-0.0.1.jar app.jar

# Expose the port your application listens on (if applicable)
EXPOSE 8080

# Command to run your Java application
CMD ["java", "-jar", "app.jar"]