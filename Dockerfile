FROM openjdk:19
COPY target/ob-rest-datajpa-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]