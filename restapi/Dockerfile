FROM openjdk:11 AS builder

COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle build.gradle
COPY settings.gradle settings.gradle
COPY src src

# RUN chmod +x ./gradlew
# RUN ./gradlew build
# RUN gradlew build

# FROM openjdk:11

# COPY --from=builder build/lib/*.jar application.jar
# EXPOSE 8080

# CMD [ "java", "-jar", "application.jar"]