FROM eclipse-temurin:22-jdk-jammy

ENV MAVEN_VERSION=3.9.1 \
    MAVEN_HOME=/opt/maven \
    M2_HOME=/opt/maven

RUN apt-get update && \
    apt-get install -y wget libfreetype6 libfontconfig1 && \
    wget -O apache-maven-$MAVEN_VERSION-bin.tar.gz https://archive.apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz && \
    tar -xzf apache-maven-$MAVEN_VERSION-bin.tar.gz -C /opt && \
    mv /opt/apache-maven-$MAVEN_VERSION /opt/maven && \
    ln -s /opt/maven/bin/mvn /usr/bin/mvn && \
    rm apache-maven-$MAVEN_VERSION-bin.tar.gz

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean install -U -DskipTests

COPY target/finance-tec-test-0.0.1-SNAPSHOT.war /app/finance-tec-test-0.0.1-SNAPSHOT.war

EXPOSE 8080

CMD ["java", "-jar", "/app/finance-tec-test-0.0.1-SNAPSHOT.war"]