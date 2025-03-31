FROM amazoncorretto:23
WORKDIR /app
COPY ./build/libs/charmander-api-core-0.0.1.jar app.jar
COPY ./whatap /app/whatap
ENTRYPOINT ["java",
"--add-opens=java.base/java.lang=ALL-UNNAMED",
"-javaagent:/app/whatap/whatap.agent-2.2.55.jar",
"-jar", "app.jar"]
