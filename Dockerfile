#FROM openjdk:11 as rabbitmq
FROM maven:3.8.2-jdk-8
WORKDIR /app

# Copy maven executable to the image
COPY mvnw .
COPY .mvn .mvn

# Copy the pom.xml file
COPY pom.xml .

# Copy the project source
COPY ./src ./src
COPY ./pom.xml ./pom.xml

RUN chmod 755 /app/mvnw
EXPOSE 8100

#RUN ./mvnw clean install -Dmaven.test.skip=true
#CMD ./mvnw spring-boot:run

#RUN ./mvnw package -DskipTests
#CMD ["java","-jar","target/notification-0.0.1-SNAPSHOT.jar","--host", "0.0.0.0", "--port", "8100"]

RUN mvn clean install -Dmaven.test.skip=true
CMD mvn spring-boot:run