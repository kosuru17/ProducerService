FROM openjdk:8
EXPOSE 8080
ADD target/producer-image.jar producer-image.jar
ENTRYPOINT ["java","-jar","/producer-image.jar"]