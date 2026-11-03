# Step 1: Build the application using Maven
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Step 2: Find and Run the application
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Yeh command poori repository me se .jar file dhoond kar /app/app.jar me copy karega
COPY --from=build /*/target/*.jar /app/app.jar || COPY --from=build /target/*.jar /app/app.jar || COPY --from=build ./**/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
