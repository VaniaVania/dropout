FROM openjdk:17

EXPOSE 8085

VOLUME /tmp

ADD target/rest-0.0.1-SNAPSHOT.jar rest-0.0.1.jar

ENTRYPOINT ["java", "-jar", "rest-0.0.1.jar"]