FROM eclipse-temurin:17
RUN mkdir app
RUN ["chmod", "+rwx", "/app"]
WORKDIR /app
COPY --chown=0:0 target/Java-SpringSecurity-0.0.1-SNAPSHOT.jar /app/
EXPOSE 8080
RUN java -version
CMD java -jar Java-SpringSecurity-0.0.1-SNAPSHOT.jar