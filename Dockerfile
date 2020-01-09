FROM openjdk:8-jdk-alpine
MAINTAINER Max Grobe <maximilian.grobe@swisslog.com>
VOLUME /tmp
EXPOSE 81 80 8200
ARG JAR_FILE
COPY target/enterprisePortal.jar app.jar
#CMD java ${JAVA_OPTS} -agentlib:jdwp=transport=dt_socket,server=y,address=8200,suspend=n -jar /app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]