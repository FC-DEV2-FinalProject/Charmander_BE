FROM amazoncorretto:23
WORKDIR /app
#TODO: jar파일 명 SNAPSHOT으로 변경
COPY ./build/libs/charmander-api-core-0.0.1.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
