FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG jar_file
ADD ${jar_file} randomator-server.jar
ARG active_environment
ENV ACTIVE_ENVIRONMENT=$active_environment
ARG port
ENV PORT=$port
ARG jasypt_encryptor_staging_password
ENV JASYPT_ENCRYPTOR_STAGING_PASSWORD=$jasypt_encryptor_staging_password
EXPOSE 8080
CMD ["java", "-Dspring.profiles.active=${ACTIVE_ENVIRONMENT}", "-Dserver.port=${PORT}", "-Djasypt.encryptor.password=${JASYPT_ENCRYPTOR_STAGING_PASSWORD}", "-jar", "randomator-server.jar"]