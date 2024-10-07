##Run SonarQube to check the codee##
FROM sonarsource/sonar-scanner-cli:latest AS sonarqube
WORKDIR /usr/src
COPY . /usr/src
COPY ./sonar-runner.properties /opt/sonar-scanner/conf/sonar-scanner.properties
RUN sonar-scanner -Dsonar.projectBaseDir=/usr/src -Dsonar.failonError=false -Dsonar.verbose=true

#Build The Artifact Package##
FROM maven:3.8.7-openjdk-18-slim AS builder
WORKDIR /my_app
COPY . /my_app/
RUN chmod +x ./mvnw
RUN ./mvnw package

###Create the final image##
FROM rgrubba/alpine-java-18 
WORKDIR /code
COPY --from=builder /my_app/target/*.jar /code/
CMD ["sh", "-c", "java", "-jar", "/code/*.jar"]