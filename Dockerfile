FROM openjdk:11
EXPOSE 7777
ARG JAR_FILE=./build/libs/fileserver-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
