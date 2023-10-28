# Builder stage
FROM openjdk:17-jdk-slim as builder
WORKDIR application
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

# Final stage
FROM openjdk:17-jdk-slim
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
EXPOSE 8080


# docker build -t book-shop:1.0 .
# docker run -p 8080:8080 book-shop:1.0

#  Listing Docker Containers - docker ps
# docker stop my-java-container
# docker images
# Removing Docker Images - docker rmi my-java-app:1.0
# Viewing Container Logs - docker logs my-java-container
#  Executing Commands Inside a Container - docker exec -it my-java-container /bin/sh (docker exec -it container_id_or_name command_to_run)
#  Pulling Docker Images from a Registry - docker pull openjdk:17-jdk-alpine

# AWS
# docker run -p 80:8080 public.ecr.aws/m8e9o5s4/bookstore-api --spring.datasource.url=jdbc:mysql://bookstore.ch2azkqd3imb.us-east-1.rds.amazonaws.com:3306/bookstore --spring.datasource.username=admin --spring.datasource.password=av212069
