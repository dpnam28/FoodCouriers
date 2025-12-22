FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests clean package

FROM eclipse-temurin:21-jre
WORKDIR /app

ENV CLOUDINARY_URL="<your_api_key>:<your_api_secret>@<your_cloud_name>" \
    DBMS_CONNECTION_URL="jdbc:mysql://<your_db_host>:<your_db_port>/<your_db_name>" \
    DBMS_PASSWORD="<your_db_password>" \
    DBMS_USERNAME="<your_db_username>"

COPY --from=builder /app/target/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
