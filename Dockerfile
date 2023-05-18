FROM maven:3.8.4-openjdk-11-slim

# Install system dependencies
RUN apt-get update && apt-get install -y \
    libc6-dev \
    libgomp1

# Copy the source code
COPY . /app

# Set the working directory
WORKDIR /app

# Build the application
RUN mvn clean package

# Run the application
CMD ["java", "-jar", "/app/target/project2-0.0.1-SNAPSHOT.jar"]