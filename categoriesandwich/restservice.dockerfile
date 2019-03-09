FROM openjdk:8
VOLUME /tmp
ADD target/categorie-sandwich-0.0.1-SNAPSHOT.jar categorie-sandwich.jar
RUN bash -c 'touch /restservice.jar'

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar", "/categorie-sandwich.jar"]