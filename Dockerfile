FROM openjdk:8-jdk-alpine as build-stage

WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw clean package
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM openjdk:8-jdk-alpine

RUN addgroup -S app-user && adduser -S app-user -G app-user
USER app-user

VOLUME /tmp

ARG DEPENDENCY=/workspace/app/target/dependency

COPY --from=build-stage ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build-stage ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build-stage ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java","-cp","app:app/lib/*","bi.social.BiSocialApplication"]
