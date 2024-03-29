
FROM openjdk:21 AS build
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew build

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/File_storage_API-0.0.1.jar .
COPY ./docker-startup.sh .
RUN chmod +x docker-startup.sh
EXPOSE 8080
CMD ["./docker-startup.sh"]



