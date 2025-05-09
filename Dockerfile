FROM eclipse-temurin:22-jdk-jammy

WORKDIR /app

COPY target/finance-tec-test-0.0.1-SNAPSHOT.war /app/app.war

EXPOSE 8080

CMD ["java", "-jar", "/app/app.war"]
