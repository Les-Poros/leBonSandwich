# leBonSandwich


Membres du projet :
- Maeva Butaye    ( Lilychaan )
- Camille Schwarz ( S-Camille )
- Léo Galassi     ( ElGitariste )
- Quentin Rimet   ( QuentinRimet )

# Prérequis :

* Maven
* Docker
* Docker-compose

## Pour lancer le service categoriesandwich

- mvn clean install -DskipTests
- docker-compose up --build -d

Celui ci sera alors accessible sur le port 8082 de votre adresse docker

## Pour lancer le service commandes

- mvn clean install -DskipTests
- docker-compose up --build -d

Celui ci sera alors accessible sur le port 8083 de votre adresse docker
