FROM openjdk:17
EXPOSE 8080
ADD target/producer-image.jar producer-image.jar
ENTRYPOINT ["java","-jar","/producer-image.jar"]