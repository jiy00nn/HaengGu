FROM azul/zulu-openjdk:11 AS builder

COPY gradle gradle
COPY src src
COPY build.gradle .
COPY gradlew .
COPY settings.gradle .

RUN chmod +x ./gradlew
RUN ./gradlew bootJar

FROM azul/zulu-openjdk:11
COPY --from=builder build/libs/*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8080
ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=prod", "/app.jar"]