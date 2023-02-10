FROM openjdk:17
EXPOSE 8085
VOLUME /tmp
ENTRYPOINT ["java", "-jar", "rest-0.0.1.jar"]