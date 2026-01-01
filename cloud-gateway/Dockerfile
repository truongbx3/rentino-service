FROM openjdk:11
WORKDIR /app
ENV TZ="Asia/Ho_Chi_Minh"
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
COPY /target/*.jar /app/app.jar
ENTRYPOINT ["java", "-jar","/app/app.jar"]
