#FROM maven:3.6.0-jdk-11-slim AS MAVEN_BUILD
#COPY . /build
#RUN #mvn  --settings /build/admin-service/settings.xml -f /build/admin-service/pom.xml clean package
#RUN #mvn -f /build/remino-common/pom.xml clean package
#RUN mvn -f /build/cloud-gateway/pom.xml clean package
#RUN mvn  -f /build/auth-service/pom.xml clean package

#FROM openjdk:11 AS admin-service
#WORKDIR /app
#ENV TZ="Asia/Ho_Chi_Minh"
#RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
#COPY --from=MAVEN_BUILD /build/admin-service/target/*.jar /app/app.jar
#ENTRYPOINT ["java", "-jar","/app/app.jar"]

FROM openjdk:11.0.11-jdk-slim AS gateway-xiaomi-service
WORKDIR /app
ENV TZ="Asia/Ho_Chi_Minh"
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
COPY /cloud-gateway/target/*.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar", "--spring.profiles.active=prod"]

FROM openjdk:11.0.11-jdk-slim AS auth-xiaomi-service
WORKDIR /app
ENV TZ="Asia/Ho_Chi_Minh"
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
COPY /auth-service/target/*.jar /app/app.jar
ENTRYPOINT ["java", "-jar","/app/app.jar"]

FROM eclipse-temurin:11-jdk AS rentino-xiaomi-service
WORKDIR /app
ENV TZ=Asia/Ho_Chi_Minh

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone && \
    apt-get update && apt-get install -y \
    libreoffice-writer \
    fonts-dejavu \
    fonts-liberation \
    --no-install-recommends && \
    fc-cache -f -v && \
    apt-get clean && rm -rf /var/lib/apt/lists/*
RUN fc-cache -f -v

RUN #mkdir -p /app/tmp   && chmod 1777 /app/tmp

ENV TZ="Asia/Ho_Chi_Minh"
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
COPY /rentino-service/target/*.jar /app/app.jar
ENTRYPOINT ["java", "-jar","/app/app.jar"]
