FROM openjdk:17-oracle
ADD build/libs/superheroes-1.0.0.jar superheroes-1.0.0.jar
ENTRYPOINT ["java", "-jar","superheroes-1.0.0.jar"]
EXPOSE 8080