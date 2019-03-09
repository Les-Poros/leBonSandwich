FROM openjdk:8
VOLUME /tmp
ADD target/commande-0.0.1-SNAPSHOT.jar commande.jar
RUN bash -c 'touch /restservice.jar'

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar", "/commande.jar"]